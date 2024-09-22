package tanoshi.multiplatform.common.db

import org.jetbrains.exposed.sql.Table
import java.io.File

class Library(
    dbDirectory : File
) : DB( DATABASE_NAME , DATABASE_VERSION , dbDirectory , ::onCreate , ::onVersionChange ) {


    private object LibraryEntry : Table() {

        val id = integer( "id" ).autoIncrement()
        val entry = text( "entry" )
        override val primaryKey = PrimaryKey(id, name = "Library_Entry_Id")

    }

    companion object {

        private const val DATABASE_NAME = "Library"
        private const val DATABASE_VERSION = 1

        private fun onCreate() {
            println( "Database Created $DATABASE_VERSION" )
        }
        private fun onVersionChange( previousVersion : Int ) {
            println( "Version Changed from $previousVersion -> $DATABASE_VERSION" )
        }
    }

}