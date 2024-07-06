package tanoshi.multiplatform.shared.extension

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.extension.core.Extension
import java.io.File
import java.io.FileInputStream

expect class ExtensionManager {
    
    val extensionLoader : ExtensionLoader
    
    fun install( extensionId : String , fileInputStream : FileInputStream )
    
    fun install( extensionId: String , file : File )
    
    fun uninstall( extensionId: String )

    fun loadExtensions()

    fun unloadExtensions()

    val Extension.icon : @Composable () -> Unit

}
