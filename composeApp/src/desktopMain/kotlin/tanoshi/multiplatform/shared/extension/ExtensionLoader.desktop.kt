package tanoshi.multiplatform.shared.extension

import tanoshi.multiplatform.common.extension.PlayableExtension
import tanoshi.multiplatform.common.extension.ReadableExtension
import tanoshi.multiplatform.common.extension.ViewableExtension
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.core.insertSharedDependencies
import tanoshi.multiplatform.common.util.logger.Logger
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.zip.ZipFile

actual class ExtensionLoader {

    lateinit var logger : Logger

    val classList : HashSet<String> = hashSetOf()

    // pair( pair(package name , archive name) , extension )
    actual val loadedExtensionClasses : ArrayList<Pair< Pair<String,String> , Extension >> = ArrayList()

    actual fun loadTanoshiExtension( vararg tanoshiExtensionFile : File ) = tanoshiExtensionFile.forEach { extensionFile ->
        try {
            loadExtension( extensionFile )
        } catch ( e : Exception ) {
            logger log {
                ERROR
                title = "Failed to load Tanoshi extension file $extensionFile"
                e.stackTraceToString()
            }
        } catch ( e : Error ){
            logger log {
                ERROR
                title = "Failed to load Tanoshi extension file $extensionFile"
                e.stackTraceToString()
            }
        } 
    }
    
    private fun loadExtension(tanoshiExtensionFile : File ) {
        val classLoader = URLClassLoader(
            arrayOf( tanoshiExtensionFile.absolutePath.url ) ,
            this.javaClass.classLoader
        )
        val classNameList : ArrayList<String> = ArrayList()
        ZipFile( tanoshiExtensionFile ).use { zip ->
            zip.entries().asSequence().forEach { zipEntry ->
                if ( zipEntry.name.endsWith( ".class" ) ) classNameList.add( zipEntry.name.replace( "/" , "." ).removeSuffix( ".class" ) )
            }
        }
        classNameList.forEach { name ->
            try {
                val loadedClass: Class<*> = classLoader.loadClass(name)
                val obj: Any = loadedClass.getDeclaredConstructor().newInstance()
                if (obj is Extension) {
                    try {
                        if ( classList.contains( name ) ) throw Exception( "Duplicate Class Found" )
                        loadedExtensionClasses.add( Pair( Pair( name , tanoshiExtensionFile.absolutePath) , obj ) )
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
            }catch ( e : Exception ) {
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

    private val Extension.injectDefaultSharedDependencies : Unit
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

    private val String.url : URL
        get() = File( this ).toURI().toURL()
    
    
}