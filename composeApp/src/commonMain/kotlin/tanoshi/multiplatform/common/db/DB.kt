package tanoshi.multiplatform.common.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import tanoshi.multiplatform.common.util.child
import java.io.File

open class DB(
    protected val dbName: String,
    protected val dbVersion: Int,
    protected val dbDirectory: File,
    onCreate: (DB) -> Unit = {} ,
    onVersionChange: (Int,DB) -> Unit = { _,_ -> }
) {

    val connection : Database
        get() {
            return Database.connect("jdbc:h2:${dbDirectory.child( dbName )}", driver = "org.h2.Driver")
        }

    fun stablishedTransaction(statement : Transaction.() -> Unit ) : Unit = transaction( connection , statement )

    protected fun rawQuery( rawSqlQuery : String , args : ArrayList<Pair<ColumnType<*>,Any>>.() -> Unit ) = stablishedTransaction {
        TransactionManager.current().connection.let { connection ->
            connection.prepareStatement(rawSqlQuery, false).let { statement ->
                statement.fillParameters(
                    arrayListOf<Pair<ColumnType<*>,Any>>().apply(args)
                )
                statement.executeUpdate()
            }
        }
    }

    private object DBInfo : Table( "DBINFO" ) {
        val verison = integer( "version" )
    }

    init {
        val isOnCreate = !dbDirectory.child("$dbName.mv.db").isFile
        stablishedTransaction {
            SchemaUtils.create(DBInfo)
            if (isOnCreate) {
                DBInfo.insert {
                    it[verison] = dbVersion
                }
                onCreate(this@DB)
            } else DBInfo.selectAll().first()[DBInfo.verison].let { databaseVersion ->
                if (databaseVersion != dbVersion) {
                    DBInfo.deleteAll()
                    DBInfo.insert {
                        it[verison] = dbVersion
                    }
                    onVersionChange(databaseVersion,this@DB)
                }
            }
        }
    }

}