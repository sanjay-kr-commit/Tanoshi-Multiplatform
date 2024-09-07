package tanoshi.multiplatform.shared.extension

import androidx.compose.foundation.Image
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.toFile
import tanoshi.multiplatform.shared.util.loadImageBitmap
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

expect class ExtensionManager {

    val extensionLoader : ExtensionLoader

    val extensionIconPath : HashMap<String,String>

    fun install( file : File )

    fun loadExtensions()

    fun unloadExtensions()

    var dir : File

    var cacheDir : File

}