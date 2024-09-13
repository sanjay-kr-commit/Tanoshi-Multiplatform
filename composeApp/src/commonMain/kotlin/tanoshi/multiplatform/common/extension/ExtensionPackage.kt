package tanoshi.multiplatform.common.extension

import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.Manifest
import java.io.File

data class ExtensionPackage(
    val jarOrDexPath : File ,
    val extensionDir : File ,
    val manifest: Manifest ,
    val loadedExtensionClasses : HashMap<String,Extension> = hashMapOf()
)