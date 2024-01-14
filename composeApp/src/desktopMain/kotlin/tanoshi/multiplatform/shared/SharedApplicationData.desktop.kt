package tanoshi.multiplatform.shared

import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowState
import tanoshi.multiplatform.common.util.currentDateTime
import tanoshi.multiplatform.common.util.logger
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.desktop.util.ExitTask
import tanoshi.multiplatform.shared.extension.ExtensionManager

actual data class SharedApplicationData(

    actual val appStartUpTime : String = currentDateTime,

    actual val logFileName : String = "$appStartUpTime.log.txt",

    actual val extensionManager : ExtensionManager = ExtensionManager(),

    actual val logger : Logger = logger(),

    val exitTasks : ExitTask = ExitTask( logger = logger , logFileName = logFileName ),

    val windowState: WindowState = WindowState() ,

) {

    lateinit var applicationScope: ApplicationScope

    var error : Throwable? = null

    actual val portrait : Boolean
        get() = windowState.size.height > windowState.size.width
    
}