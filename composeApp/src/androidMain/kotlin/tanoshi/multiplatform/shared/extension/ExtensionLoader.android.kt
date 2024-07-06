package tanoshi.multiplatform.shared.extension

import android.content.Context
import tanoshi.multiplatform.common.extension.core.Extension
import java.io.File
import java.lang.Exception
import dalvik.system.PathClassLoader
import tanoshi.multiplatform.common.extension.PlayableExtension
import tanoshi.multiplatform.common.extension.ReadableExtension
import tanoshi.multiplatform.common.extension.ViewableExtension
import tanoshi.multiplatform.common.extension.core.insertSharedDependencies
import tanoshi.multiplatform.common.util.logger.Logger

actual class ExtensionLoader {

    lateinit var logger : Logger

    lateinit var applicationContext : Context

    val classList : HashSet<String> = hashSetOf()

    // pair( pair(package name , archive name) , extension )
    actual val loadedExtensionClasses : ArrayList< Pair<Pair<String,String> , Extension> > = ArrayList()

    actual fun loadTanoshiExtension( vararg tanoshiExtensionFile : String ) {
        if ( !::applicationContext.isInitialized ) throw UninitializedPropertyAccessException( "application context not initialised" )
        tanoshiExtensionFile.forEach { tanoshiFile ->
            loadTanoshiFile( tanoshiFile )
        }
    }
    
    private fun loadTanoshiFile( tanoshiExtensionFile: String ) = applicationContext.run {
        val dexFiles : List<String> = getDir( "extension/$tanoshiExtensionFile" , Context.MODE_PRIVATE ).listFiles().run {
            val listOfDexFile = ArrayList<String>()
            forEach { file ->
                val fileString = file.toString()
                if ( fileString.endsWith( ".dex" ) ) listOfDexFile.add(
                    if ( fileString.contains( "/" ) ) fileString.substring( fileString.lastIndexOf( "/" )+1 )
                    else fileString
                )
            }
            listOfDexFile
        }
        dexFiles.forEach { dexName ->
            try {
                loadDexFile( tanoshiExtensionFile , dexName )
            } catch ( _ : Exception ) {
            } catch ( _ : Error ) { }
        }
    }
    
    private fun loadDexFile( tanoshiExtensionFile: String , dexFileName : String ) = applicationContext.run {
        val file = File( getDir( "extension/$tanoshiExtensionFile" , Context.MODE_PRIVATE ) , dexFileName )
        val classLoader = PathClassLoader( file.absolutePath , classLoader )
        val classNameList : List<String> = Regex( "L[a-zA-Z0-9/]*;" ).findAll( file.readText() ).run {
            val nameList = ArrayList<String>( count() )
            forEach {  lClassPath ->
                nameList.add(
                    lClassPath.value.substring( 1 , lClassPath.value.length-1 ).replace( "/" , "." )
                )
            }
            nameList
        }
        classNameList.forEach { name ->
            try {
                val loadedClass : Class<*> = classLoader.loadClass( name )
                val obj : Any = loadedClass.getDeclaredConstructor().newInstance()
                if (obj is Extension) {
                    try {
                        if ( classList.contains( name ) ) throw Exception( "Duplicate Class Found" )
                        loadedExtensionClasses.add( Pair( Pair( name , tanoshiExtensionFile) , obj ) )
                        classList.add( name )
                        logger log {
                            DEBUG
                            title = "Loaded Extension $name"
                            """
                               |Name          : ${obj.name}
                               |Type          : ${
                                when ( obj ) {
                                    is PlayableExtension -> "Playable Extension"
                                    is ReadableExtension -> "Readable Extension"
                                    is ViewableExtension -> "Viewable Extension"
                                    else -> "Unknown Extended Extension"
                                }
                            }
                               |Language      : ${obj.language}
                               |Package       : $name
                               |Domain List   : ${obj.domainsList.entries().toList()}
                            """.trimMargin("|")
                        }
                        obj.injectDefaultSharedDependencies
                    } catch ( e : Exception ) {
                        logger log {
                            ERROR
                            title = "Failed to load Extension : $name"
                            e.stackTraceToString()
                        }
                    } catch ( e : Error ) {
                        logger log {
                            ERROR
                            title = "Failed to load Extension : $name"
                            e.stackTraceToString()
                        }
                    }
                }
            } catch ( e : Exception ) {
                logger log {
                    ERROR
                    title = "Failed to load class : $name"
                    e.stackTraceToString()
                }
            } catch ( e : Error ) {
                logger log {
                    ERROR
                    title = "Failed to load class : $name"
                    e.stackTraceToString()
                }
            }
        }
    }

    val Extension.injectDefaultSharedDependencies : Unit
        get() {
            insertSharedDependencies {
                logger = this@ExtensionLoader.logger
                logger log {
                    DEBUG
                    title = "$name injected SharedDependencies"
                    """
                        |Injected Logger
                    """.trimMargin()
                }
            }
        }
    
}