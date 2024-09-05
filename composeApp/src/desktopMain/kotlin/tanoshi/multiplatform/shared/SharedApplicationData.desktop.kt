package tanoshi.multiplatform.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.Job
import tanoshi.multiplatform.common.util.currentDateTime
import tanoshi.multiplatform.common.util.logger
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.shared.extension.ExtensionManager
import java.io.File
import java.util.ArrayDeque

actual open class SharedApplicationData(

    actual val appStartUpTime : String = currentDateTime,

    actual val logFileName : String = "$appStartUpTime.log.txt",

    actual val extensionManager : ExtensionManager = ExtensionManager(),

    actual val logger : Logger = logger(),

    val windowState: WindowState = WindowState() ,

    actual var publicDir: File = File( System.getProperty( "user.dir" ) , "tanoshi" ) ,

    actual var privateDir : File = File( System.getProperty( "user.dir" ) , ".tanoshi" ) ,

    actual var appCacheDir: File = File( System.getProperty( "user.dir" )  , ".tanoshi/cache" )

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
        logger log {
            title = "App Directory"
            publicDir.absolutePath
        }
        extensionManager.dir = File( privateDir , "extensions" )
        extensionManager.logger = logger
        extensionManager.extensionLoader.logger = logger
        extensionManager.loadExtensions()
    }

    val toastDeque : ArrayDeque<Pair<String,Long>> = ArrayDeque()
    var isToastWindowVisible : Boolean by mutableStateOf( false )
    var toastJob : Job? = null
    var toastMessage : String by mutableStateOf( "" )

}