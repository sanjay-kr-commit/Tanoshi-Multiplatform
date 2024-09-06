package tanoshi.multiplatform.shared.extension

import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.logger.Logger
import java.io.File

actual class ExtensionLoader {

    lateinit var logger: Logger

    lateinit var classLoader : ClassLoader

    // pair( pair(package name , archive name) , extension )
    actual val loadedExtensionClasses : ArrayList< Pair< Pair<String,String> , Extension> > = arrayListOf()

    actual fun loadTanoshiExtension(
        jarOrDexFile: File,
        classNameList: List<String>
    ) {
        TODO()
    }


}