package tanoshi.multiplatform.shared.extension

import tanoshi.multiplatform.common.extension.core.Extension

expect class ExtensionLoader {

    // pair( pair(package name , archive name) , extension )
    val loadedExtensionClasses : ArrayList< Pair< Pair<String,String> , Extension> >
    
    fun loadTanoshiExtension( vararg tanoshiExtensionFile : String )

}