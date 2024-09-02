package tanoshi.multiplatform.shared

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import tanoshi.multiplatform.common.util.currentDateTime
import tanoshi.multiplatform.common.util.logger
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.shared.extension.ExtensionManager
import java.io.File

actual open class SharedApplicationData(

    actual val appStartUpTime : String = currentDateTime,

    actual val logFileName : String = "$appStartUpTime.log.txt",

    actual val extensionManager : ExtensionManager = ExtensionManager(),

    actual val logger : Logger = logger() ,

    var startCrashActivity : () -> Unit = {}
    
) : Application() {
    
    var _portrait : Boolean by mutableStateOf( true )
    
    actual val portrait : Boolean
        get() = _portrait

    init {
        logger log {
            title = "App Start Up Time"
            "App Started At $appStartUpTime"
        }
        extensionManager.logger = logger
        extensionManager.extensionLoader.logger = logger
    }

    actual var appDir: File = File( "" )

}