package tanoshi.multiplatform.common.db

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import tanoshi.multiplatform.common.extension.*
import tanoshi.multiplatform.common.extension.core.Extension
import java.io.File

class Library(
    dbDirectory : File
) : DB(
    DATABASE_NAME ,
    DATABASE_VERSION ,
    dbDirectory ,
    ::onCreate ,
    ::onVersionChange
) {

    private val _entries_ : ArrayList<String> = arrayListOf()
    val entries : List<String>
        get() = _entries_

    private object LibraryEntry : Table( "LIBRARY_ENTRY" ) {
        val url = text( "ENTRY_URL" )
        val serializedEntry = text( "SERIALIZED_ENTRY" )
        val entryType = text( "CLASS_NAME" )

        val extensionName = text( "EXTENSION_CLASS_NAME" )

        val extensionPackageName = text( "EXTENSION_PACKAGE_NAME" )

        override val primaryKey: PrimaryKey = PrimaryKey( url , name = "PRIMARY_KEY" )
    }

    init {
        stablishedTransaction {
            LibraryEntry.selectAll().forEach {
                _entries_ += it[LibraryEntry.url]
            }
        }
    }

    companion object {

        private const val DATABASE_NAME = "Library"
        private const val DATABASE_VERSION = 1

        private fun onCreate(db:DB) = db.stablishedTransaction {
            SchemaUtils.create( LibraryEntry )
        }
        private fun onVersionChange( previousVersion : Int , db:DB ) {
            SchemaUtils.drop( LibraryEntry )
            SchemaUtils.create( LibraryEntry )
        }
    }

    fun insert( entry : Entry<*> , extension : Extension<*> , extensionPackage: ExtensionPackage ) = stablishedTransaction {
        LibraryEntry.insert {
            it[url] = entry.url!!
            it[extensionPackageName] = extensionPackage::class.java.name
            it[extensionName] = extension::class.java.name
            it[serializedEntry] = when ( entry ) {
                is PlayableEntry -> {
                    it[entryType] = PlayableEntry::class.java.name
                    val typedEntry = entry as PlayableEntry
                    Json.encodeToString( typedEntry )
                }
                is ReadableEntry -> {
                    it[entryType] = ReadableEntry::class.java.name
                    val typedEntry = entry as ReadableEntry
                    Json.encodeToString( typedEntry )

                }
                is ViewableEntry -> {
                    it[entryType] = ViewableEntry::class.java.name
                    val typedEntry = entry as ViewableEntry
                    Json.encodeToString( typedEntry )
                }
                else -> throw Exception( "Unknown Entry" )
            }
        }
        _entries_.add( entry.url!! )
    }

    fun update( entry: Entry<*> ) = stablishedTransaction {
        LibraryEntry.update {
            it[url] = entry.url!!
            it[serializedEntry] = when ( entry ) {
                is PlayableEntry -> {
                    it[entryType] = PlayableEntry::class.java.name
                    val typedEntry = entry as PlayableEntry
                     Json.encodeToString( typedEntry )
                }
                is ReadableEntry -> {
                    it[entryType] = ReadableEntry::class.java.name
                    val typedEntry = entry as ReadableEntry
                    Json.encodeToString( typedEntry )

                }
                is ViewableEntry -> {
                    it[entryType] = ViewableEntry::class.java.name
                    val typedEntry = entry as ViewableEntry
                    Json.encodeToString( typedEntry )
                }
                else -> throw Exception( "Unknown Entry" )
            }
        }
    }

    fun remove( entry: Entry<*> ) = stablishedTransaction {
        LibraryEntry.deleteWhere {
            url like entry.url!!
        }
        _entries_.remove( entry.url!! )
    }

    fun contains( entry : Entry<*> ) : Boolean = _entries_.contains( entry.url!! )

    fun selectAll( iterationScope : Entry<*>.( String , String ) -> Unit ) = stablishedTransaction {
        LibraryEntry.selectAll().forEach {
            when ( it[LibraryEntry.entryType] ) {
                PlayableEntry::class.java.name -> Json.decodeFromString<PlayableEntry>( it[LibraryEntry.serializedEntry] )
                ReadableEntry::class.java.name -> Json.decodeFromString<ReadableEntry>( it[LibraryEntry.serializedEntry] )
                ViewableEntry::class.java.name -> Json.decodeFromString<ViewableEntry>( it[LibraryEntry.serializedEntry] )
                else -> throw Exception( "Unknown Entry" )
            }.iterationScope( it[LibraryEntry.extensionName] , it[LibraryEntry.extensionPackageName] )
        }
    }

    fun toList() : List<Entry<*>> = arrayListOf<Entry<*>>().apply {
        selectAll { _,_->
            add( this )
        }
    }

}