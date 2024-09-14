package tanoshi.multiplatform.shared

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import tanoshi.multiplatform.common.util.currentDateTime
import tanoshi.multiplatform.common.util.logger
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.shared.extension.ExtensionManager
import tanoshi.multiplatform.common.util.ApplicationActivityName
import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual open class SharedApplicationData(

    actual val appStartUpTime : String = currentDateTime,

    actual val logFileName : String = "$appStartUpTime.log.txt",

    actual val extensionManager : ExtensionManager = ExtensionManager(),

    actual val logger : Logger = logger() ,

    actual var publicDir: File = File( "" ) ,

    actual var privateDir : File = File("")

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

    actual var appCacheDir: File = File( "cache" )

    lateinit var showToastLambda : ( String , Int ) -> Unit

    actual var activityMap : Map<ApplicationActivityName,()->Unit>? = null
    
    actual var extensionComposableView : @Composable () -> Unit = {}

    var exitActivity : () -> Unit = {}

}

actual val SharedApplicationData.finish: Unit
    get() = exitActivity()