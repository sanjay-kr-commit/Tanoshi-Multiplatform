package tanoshi.multiplatform.shared.extension

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipFile

actual class ExtensionManager {
    
    private lateinit var _applicationContext : Context
    
    actual val extensionLoader: ExtensionLoader = ExtensionLoader()
    var applicationContext : Context
        get() = _applicationContext
        set(value) {
            extensionLoader.applicationContext = value
            _applicationContext = value
        }
    
    actual fun install( extensionId: String , file: File ) {
        install( extensionId , file.inputStream() )
    }
    
    actual fun install(extensionId: String, fileInputStream: FileInputStream) : Unit = _applicationContext.run {
        val extensionDir = getDir( "extension/$extensionId" , Context.MODE_PRIVATE ).also {
            if ( !it.isDirectory ) it.mkdirs()
        }
        val extensionFile = File( extensionDir , "extension.tanoshi" )
        extensionFile.outputStream().use { output ->
            fileInputStream.use { input ->
                val buffer = ByteArray( 1024 )
                var len = 0
                while ( input.read( buffer ).also { len = it } > 0 ) {
                    output.write( buffer , 0 , len )
                }
            }
        }
        
        ZipFile( extensionFile ).use { extension ->
            extension.entries().asSequence().forEach { entry ->
                val name = entry.name
                if ( name.endsWith( ".dex" ) ) {
                    extension.getInputStream( entry ).use { input ->
                        File(
                            extensionDir ,
                            if ( name.contains( "/" ) ) name.substring( name.lastIndexOf( "/" )+1 )
                            else name
                        ).outputStream().use { output ->
                            val buffer = ByteArray( 1024 )
                            var len = 0
                            while ( input.read( buffer ).also { len = it } > 0 ) {
                                output.write( buffer , 0 , len )
                            }
                        }
                    }
                }
            }
        }
    }

    actual fun uninstall(extensionId: String) : Unit = _applicationContext.run {
        getDir( "extension/$extensionId" , Context.MODE_PRIVATE ).deleteRecursively()
    }

}