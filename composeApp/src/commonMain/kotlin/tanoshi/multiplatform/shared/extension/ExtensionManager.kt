package tanoshi.multiplatform.shared.extension

import java.io.File
import java.io.FileInputStream

expect class ExtensionManager {
    
    val extensionLoader : ExtensionLoader
    
    fun install( extensionId : String , fileInputStream : FileInputStream )
    
    fun install( extensionId: String , file : File )
    
    fun uninstall( extensionId: String )
    
}