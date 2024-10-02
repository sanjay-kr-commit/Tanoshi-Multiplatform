package tanoshi.multiplatform.common.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import java.io.File


object Preferences {

    private object PreferenceTable : Table( "PreferenceTable" ) {
        val group = text( "GROUP" )
        val key = text( "KEY" )
        val value = text( "VALUE" )
        override val primaryKey: PrimaryKey = PrimaryKey( key , name = "Primary_Key" )
    }

    private class PreferencesClass(
        parentPath : File
    ) : DB(
        DATABASE_NAME ,
        DATABASE_VERSION ,
        parentPath ,
        ::onCreate ,
        ::onVersionChange
    )

    private lateinit var preference : PreferencesClass

    fun initializePreference( parentPath: File ) {
        preference = PreferencesClass( parentPath )
    }

    private const val DATABASE_NAME = "Preferences"

    private const val DATABASE_VERSION = 1

    private fun onCreate(db:DB) = db.stablishedTransaction {
        SchemaUtils.create( PreferenceTable )
    }
    private fun onVersionChange( previousVersion : Int , db:DB ) = db.stablishedTransaction  {
        SchemaUtils.drop( PreferenceTable )
        SchemaUtils.create( PreferenceTable )
    }

    val String.preferenceByGroupName : Map<String,String>
        get() = HashMap<String, String>().also { table ->
            preference.run {
                stablishedTransaction {
                    PreferenceTable.selectAll()
                        .where(PreferenceTable.group eq this@preferenceByGroupName)
                        .forEach { entry ->
                            table[entry[PreferenceTable.key]] = entry[PreferenceTable.value]
                        }
                }
            }
        }

    val String.withPrefix : Map<String,String>
        get() = HashMap<String,String>().also { table ->
            preference.run {
                stablishedTransaction {
                    PreferenceTable.selectAll()
                        .where( PreferenceTable.group like "${this@withPrefix}%")
                        .forEach { entry ->
                            table[entry[PreferenceTable.key]] = entry[PreferenceTable.value]
                        }
                }
            }
        }

    val String.deleteGroup : Unit
        get() {
            preference.run {
                stablishedTransaction {
                    PreferenceTable.deleteWhere {
                        this.group eq this@deleteGroup
                    }
                }
            }
        }

    fun addPreference( key : String , value : String , group : String = "Global" ) = preference.run {
        stablishedTransaction {
            PreferenceTable.insert {
                it[PreferenceTable.group] = group
                it[PreferenceTable.key] = key
                it[PreferenceTable.value] = value
            }
        }
    }

    fun removePreference( key : String ) = preference?.run {
        stablishedTransaction {
            PreferenceTable.deleteWhere {
                this.key eq key
            }
        }
    }

    infix fun String.updatePreference( newValue : String ) = preference.run {
        stablishedTransaction {
            PreferenceTable
                .update( {
                    PreferenceTable.key eq this@updatePreference
                } ) {
                    it[PreferenceTable.value] = newValue
                }
        }
    }

}