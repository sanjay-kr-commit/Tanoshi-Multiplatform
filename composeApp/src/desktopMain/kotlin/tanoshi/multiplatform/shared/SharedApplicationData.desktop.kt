package tanoshi.multiplatform.shared

import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowState
import tanoshi.multiplatform.common.util.currentDateTime
import tanoshi.multiplatform.common.util.logger
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.shared.extension.ExtensionManager

actual open class SharedApplicationData(

    actual val appStartUpTime : String = currentDateTime,

    actual val logFileName : String = "$appStartUpTime.log.txt",

    actual val extensionManager : ExtensionManager = ExtensionManager(),

    actual val logger : Logger = logger(),

    val windowState: WindowState = WindowState() ,

) {

    lateinit var applicationScope: ApplicationScope

    var error : Throwable? = null

    actual val portrait : Boolean
        get() = windowState.size.height > windowState.size.width

    init {
        logger log {
            title = "App Start Up Time"
            "App Started At $appStartUpTime"
        }
        extensionManager.logger = logger
        extensionManager.extensionLoader.logger = logger
        extensionManager.dir.listFiles()?.forEach { extensionId ->
            extensionId.listFiles()?.forEach { jar ->
                extensionManager.extensionLoader.loadTanoshiExtension( jar.absolutePath )
            }
        }
    }
    
}