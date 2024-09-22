package tanoshi.multiplatform.common.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import tanoshi.multiplatform.common.util.child
import java.io.File

open class DB(
    val dbName : String,
    val dbVersion : Int,
    val dbDirectory : File,
    onCreate : () -> Unit,
    onVersionChange : ( Int ) -> Unit
) {

    val connection : Database
        get() {
            return Database.connect("jdbc:h2:${dbDirectory.child( dbName )}", driver = "org.h2.Driver")
        }

    fun statblishedTransaction( statement : Transaction.() -> Unit ) : Unit = transaction( connection , statement ) 

    private object DBInfo : Table( "DBINFO" ) {
        val verison = integer( "version" )
    }

    init {
        val isOnCreate = !dbDirectory.child("$dbName.mv.db").isFile
        transaction (
            connection
        ) {
            SchemaUtils.create(DBInfo)
            if ( isOnCreate ) {
                DBInfo.insert {
                    it[verison] = dbVersion
                }
                onCreate()
            } else DBInfo.selectAll().first()[DBInfo.verison].let { databaseVersion ->
                if ( databaseVersion != dbVersion ) {
                    DBInfo.deleteAll()
                    DBInfo.insert {
                        it[verison] = dbVersion
                    }
                    onVersionChange(databaseVersion)
                }
            }
        }
    }

}