package tanoshi.multiplatform.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var _portrait : Boolean by mutableStateOf( true )

    actual val portrait : Boolean
        get() = _portrait
    
}