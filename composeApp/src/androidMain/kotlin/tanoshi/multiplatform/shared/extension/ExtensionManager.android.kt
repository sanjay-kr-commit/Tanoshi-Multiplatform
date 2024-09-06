package tanoshi.multiplatform.shared.extension

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.logger.Logger
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

actual class ExtensionManager {

    lateinit var logger: Logger

    private lateinit var _classLoader_ : ClassLoader
    var classLoader : ClassLoader
        set(value) {
            logger log {
                title = "Initialised Class Loader"
                """
                    Classloader attached to Extension Manager & Loader
                """.trimIndent()
            }
            _classLoader_ = value
            extensionLoader.classLoader = value
        }
        get() = _classLoader_

    actual var dir: File = File("")
    actual var cacheDir : File = File( "" )

    actual val extensionLoader: ExtensionLoader = ExtensionLoader()

    actual fun install( file: File ) {
        extractExtension(file, logger)?.let { extensionDir ->
            extensionDir.setWritable(false)
            extensionDir.setReadOnly()
            extensionDir.setExecutable(true)
            TODO()
        }
    }

    actual fun loadExtensions() {
        TODO()
    }

    actual fun unloadExtensions() {
    }

    actual val Extension.icon: @Composable () -> Unit
        get() = {

        }

}