package tanoshi.multiplatform.shared.extension

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.loadExtensionPermission
import tanoshi.multiplatform.common.util.child
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.toast.ToastTimeout
import java.io.File
import java.net.URL
import java.net.URLClassLoader

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ExtensionLoader {

    actual var startDynamicActivity : ((@Composable ()->Unit).() -> Unit)? = null

    actual var logger : Logger? = null

    actual var setToastLambda : (String.(ToastTimeout)->Unit)? = null

    val classList : HashSet<String> = hashSetOf()

    // pair( pair(package name , archive name) , extension )
    actual val loadedExtensionClasses : ArrayList<Pair< Pair<String,String> , Extension >> = ArrayList()

    actual fun loadTanoshiExtension(jarOrDexFile : File, classNameList : List<String> ) {
        val classLoader = URLClassLoader(
            arrayOf( jarOrDexFile.absolutePath.url ) ,
            this.javaClass.classLoader
        )
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

    private val String.url : URL
        get() = File( this ).toURI().toURL()
    
    
}