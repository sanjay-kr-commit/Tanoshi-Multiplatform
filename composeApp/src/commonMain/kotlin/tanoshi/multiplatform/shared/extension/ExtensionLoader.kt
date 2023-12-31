package tanoshi.multiplatform.shared.extension

import tanoshi.multiplatform.common.extension.interfaces.Extension

expect class ExtensionLoader {
    
    val loadedExtensionClasses : HashMap< String , Extension<*> >
    
    fun loadTanoshiExtension( vararg tanoshiExtensionFile : String )
    
}