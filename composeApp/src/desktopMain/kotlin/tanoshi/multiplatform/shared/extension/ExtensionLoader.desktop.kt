package tanoshi.multiplatform.shared.extension

import java.io.File
import java.net.URL
import java.util.zip.ZipFile
import java.net.URLClassLoader
import tanoshi.multiplatform.common.extension.interfaces.Extension
import tanoshi.multiplatform.common.extension.annotations.EXTENSION

actual class ExtensionLoader {

    actual val loadedExtensionClasses : HashMap< String , Extension<*> > = HashMap()

    actual fun loadTanoshiExtension( vararg tanoshiExtensionFile : String ) = tanoshiExtensionFile.forEach { extensionFile ->
        try {
            loadExtension( extensionFile )
        } catch ( _ : Exception ) {
        } catch ( _ : Error ){
        } 
    }
    
    private fun loadExtension(tanoshiExtensionFile : String ) {
        val classLoader = URLClassLoader(
            arrayOf( tanoshiExtensionFile.url ) ,
            this.javaClass.classLoader
        )
        val classNameList : ArrayList<String> = ArrayList()
        ZipFile( tanoshiExtensionFile ).use { zip ->
            zip.entries().asSequence().forEach { zipEntry ->
                if ( zipEntry.name.endsWith( ".class" ) ) classNameList.add( zipEntry.name.replace( "/" , "." ).removeSuffix( ".class" ) )
            }
        }
        classNameList.forEach { name ->
            try {
                val loadedClass = classLoader.loadClass(name)
                if ( loadedClass.annotations.any { it == EXTENSION::class.java } ) loadedExtensionClasses[ name ] = loadedClass.getDeclaredConstructor().newInstance() as Extension<*>
            } catch ( _ : Exception ) {
            } catch ( _ : Error ) {
            }
        }
    }

    private val String.url : URL
        get() = File( this ).toURI().toURL()
    
    
}