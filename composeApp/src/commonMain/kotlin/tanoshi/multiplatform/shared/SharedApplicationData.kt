package tanoshi.multiplatform.shared

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import tanoshi.multiplatform.common.db.Library
import tanoshi.multiplatform.common.util.ApplicationActivityName
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.shared.extension.ExtensionManager
import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class SharedApplicationData {
    
    val appStartUpTime : String

    val logFileName : String 

    val extensionManager : ExtensionManager 

    val logger : Logger

    val portrait : Boolean

    var publicDir : File

    var privateDir : File

    var appCacheDir : File

    var activityMap : Map<ApplicationActivityName,()->Unit>?

    var extensionComposableView : @Composable () -> Unit

    var exportedObjects : HashMap<String,Any>?

    val coroutineIoScope : CoroutineScope

    val coroutineDefaultScope : CoroutineScope

    val coroutineUnconfinedScope : CoroutineScope

    val library : Library

}

fun SharedApplicationData.changeActivity( newActivityName: ApplicationActivityName , exportObject : HashMap<String,Any>.() -> Unit = {} ) {
    exportedObjects = HashMap<String,Any>()
        .also(exportObject)
    changeActivity = newActivityName
}

var SharedApplicationData.changeActivity : ApplicationActivityName
    get() = ApplicationActivityName.Undefined
    set(newActivityName) {
        try {
            activityMap?.get(newActivityName)?.invoke() ?: run {
                logger log {
                    ERROR
                    title = "Activity Navigation Failed"
                    "$newActivityName No Such Activity Found in Acitivity Map"
                }
            }
        } catch ( e : Exception ) {
            println( e.stackTraceToString() )
            logger log {
                ERROR
                title = "Activity Navigation Failed"
                e.stackTraceToString()
            }
        }
    }

expect val SharedApplicationData.finish : Unit