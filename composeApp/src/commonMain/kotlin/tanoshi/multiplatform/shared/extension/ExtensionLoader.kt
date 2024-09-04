package tanoshi.multiplatform.shared.extension

import tanoshi.multiplatform.common.extension.core.Extension
import java.io.File

expect class ExtensionLoader {

    // pair( pair(package name , archive name) , extension )
    val loadedExtensionClasses : ArrayList< Pair< Pair<String,String> , Extension> >
    
    fun loadTanoshiExtension( vararg tanoshiExtensionFile : File )

}