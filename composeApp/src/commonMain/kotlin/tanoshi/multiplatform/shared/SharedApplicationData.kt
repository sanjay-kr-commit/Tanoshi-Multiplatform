package tanoshi.multiplatform.shared

import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.shared.extension.ExtensionManager
import java.io.File

expect class SharedApplicationData {
    
    val appStartUpTime : String

    val logFileName : String 

    val extensionManager : ExtensionManager 

    val logger : Logger

    val portrait : Boolean

    var publicDir : File

    var privateDir : File

    var appCacheDir : File

}