package tanoshi.multiplatform.shared.extension

import dalvik.system.DexClassLoader
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.core.insertSharedDependencies
import tanoshi.multiplatform.common.util.logger.Logger
import java.io.File

actual class ExtensionLoader {

    lateinit var logger: Logger

    val classList : HashSet<String> = hashSetOf()

    lateinit var appClassLoader : ClassLoader

    // pair( pair(package name , archive name) , extension )
    actual val loadedExtensionClasses : ArrayList< Pair< Pair<String,String> , Extension> > = arrayListOf()

    actual fun loadTanoshiExtension(
        jarOrDexFile: File,
        classNameList: List<String>
    ) {
        val classLoader = DexClassLoader( jarOrDexFile.absolutePath , null , null , appClassLoader )
        classNameList.forEach { className ->
            try {
                val loadedClass: Class<*> = classLoader.loadClass(className)
                val obj: Any = loadedClass.getDeclaredConstructor().newInstance()
                if ( classList.contains( className ) ) throw Exception( "Duplicate Class Found" )
                loadedExtensionClasses += ( className to jarOrDexFile.absolutePath ) to obj as Extension
                ( obj  as Extension ).injectDefaultSharedDependencies
                classList.add(className )
            } catch ( e : Exception ) {
                logger log {
                    ERROR
                    title = "Failed To Load $className"
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

}