package tanoshi.multiplatform.shared.extension

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.extension.ExtensionPackage
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

    actual val loadedExtensionPackage : ArrayList<ExtensionPackage> = arrayListOf()

    actual fun loadTanoshiExtension( extensionPackage: ExtensionPackage, classNameList : List<String> ) {
        val classLoader = URLClassLoader(
            arrayOf( extensionPackage.jarOrDexPath.absolutePath.url ) ,
            this.javaClass.classLoader
        )
        loadedExtensionPackage += extensionPackage
        classNameList.forEach { className ->
            try {
                val loadedClass: Class<*> = classLoader.loadClass(className)
                val obj: Any = loadedClass.getDeclaredConstructor().newInstance()
                if ( classList.contains( className ) ) throw Exception( "Duplicate Class Found" )
                extensionPackage.loadedExtensionClasses += className to obj as Extension
                loadExtensionPermission( className , obj as Extension , extensionPackage.extensionDir.child( "$className.config" ) )
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

    actual fun reloadClass( className : String , extensionPackage: ExtensionPackage ) {
        try {
            val classLoader = URLClassLoader(
                arrayOf(extensionPackage.jarOrDexPath.absolutePath.url),
                this.javaClass.classLoader
            )
            val loadedClass: Class<*> = classLoader.loadClass(className)
            val obj: Any = loadedClass.getDeclaredConstructor().newInstance()
            loadExtensionPermission(
                className,
                obj as Extension,
                extensionPackage.extensionDir.child("$className.config")
            )
            extensionPackage.loadedExtensionClasses[className] = obj
        } catch ( e : Exception ) {
            logger?. log {
                ERROR
                title = "Failed To Reload Class : $className"
                e.stackTraceToString()
            }
        }
    }

}