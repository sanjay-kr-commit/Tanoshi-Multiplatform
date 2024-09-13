package tanoshi.multiplatform.shared.extension

import androidx.compose.runtime.Composable
import dalvik.system.DexClassLoader
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.core.insertSharedDependencies
import tanoshi.multiplatform.common.extension.loadExtensionPermission
import tanoshi.multiplatform.common.util.child
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.toast.ToastTimeout
import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ExtensionLoader {

    actual var startDynamicActivity : ((@Composable ()->Unit).() -> Unit)? = null

    actual var logger: Logger? = null

    actual var setToastLambda : (String.(ToastTimeout)->Unit)? = null

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
                jarOrDexFile.parentFile?.let { loadExtensionPermission( className , obj as Extension , it.child( "$className.config" ) ) }
                classList.add(className )
            } catch ( e : Exception ) {
                logger?. log {
                    ERROR
                    title = "Failed To Load $className"
                    e.stackTraceToString()
                }
            }
        }
    }

}