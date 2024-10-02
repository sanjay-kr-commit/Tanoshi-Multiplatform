package tanoshi.multiplatform.shared.extension

import androidx.compose.runtime.Composable
import dalvik.system.DexClassLoader
import tanoshi.multiplatform.common.extension.ExtensionPackage
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.loadExtensionPermission
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.toast.ToastTimeout

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ExtensionLoader {

    actual var startDynamicActivity : ((@Composable ()->Unit).() -> Unit)? = null

    actual var logger: Logger? = null

    actual var setToastLambda : (String.(ToastTimeout)->Unit)? = null

    val classList : HashSet<String> = hashSetOf()

    lateinit var appClassLoader : ClassLoader

    actual val loadedExtensionPackage : ArrayList<ExtensionPackage> = arrayListOf()

    actual fun loadTanoshiExtension(
        extensionPackage: ExtensionPackage,
        classNameList: List<String>
    ) {
        val classLoader = DexClassLoader( extensionPackage.jarOrDexPath.absolutePath , null , null , appClassLoader )
        loadedExtensionPackage.add( extensionPackage )
        classNameList.forEach { className ->
            try {
                val loadedClass: Class<*> = classLoader.loadClass(className)
                val obj: Any = loadedClass.getDeclaredConstructor().newInstance()
                if ( classList.contains( className ) ) throw Exception( "Duplicate Class Found" )
                extensionPackage.loadedExtensionClasses += className to obj as Extension<*>
                loadExtensionPermission(className, obj as Extension<*>,extensionPackage.manifest.extensionNamespace!!)
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

    actual fun reloadClass( className : String , extensionPackage: ExtensionPackage ) {
        try {
            val classLoader = DexClassLoader( extensionPackage.jarOrDexPath.absolutePath , null , null , appClassLoader )
            val loadedClass: Class<*> = classLoader.loadClass(className)
            val obj: Any = loadedClass.getDeclaredConstructor().newInstance()
            loadExtensionPermission(
                className,
                obj as Extension<*> ,
                extensionPackage.manifest.extensionNamespace!!
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