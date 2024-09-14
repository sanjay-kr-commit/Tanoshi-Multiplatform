package tanoshi.multiplatform.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.Job
import tanoshi.multiplatform.common.util.ApplicationActivityName
import tanoshi.multiplatform.common.util.currentDateTime
import tanoshi.multiplatform.common.util.logger
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.desktop.util.WindowStack
import tanoshi.multiplatform.shared.extension.ExtensionManager
import java.io.File
import java.util.ArrayDeque

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual open class SharedApplicationData(

    actual val appStartUpTime : String = currentDateTime,

    actual val logFileName : String = "$appStartUpTime.log.txt",

    actual val extensionManager : ExtensionManager = ExtensionManager(),

    actual val logger : Logger = logger(),

    val windowState: WindowState = WindowState() ,

    actual var publicDir: File = File( System.getProperty( "user.dir" ) , "tanoshi" ) ,

    actual var privateDir : File = File( System.getProperty( "user.dir" ) , ".tanoshi" ) ,

    actual var appCacheDir: File = File( System.getProperty( "user.dir" )  , ".tanoshi/cache" ) ,

    actual var extensionComposableView : @Composable () -> Unit = {} ,
    
    actual var exportedObjects : HashMap<String,Any>? = null

) {

    lateinit var applicationScope: ApplicationScope

    var error : Throwable? = null

    actual val portrait : Boolean
        get() = windowState.size.height > windowState.size.width

    val toastDeque : ArrayDeque<Pair<String,Long>> = ArrayDeque()
    var isToastWindowVisible : Boolean by mutableStateOf( false )
    var toastJob : Job? = null
    var toastMessage : String by mutableStateOf( "" )

    actual var activityMap : Map<ApplicationActivityName,()->Unit>? = null

    lateinit var windowStack : WindowStack

}

actual val SharedApplicationData.finish: Unit
    get() = windowStack.pop()
