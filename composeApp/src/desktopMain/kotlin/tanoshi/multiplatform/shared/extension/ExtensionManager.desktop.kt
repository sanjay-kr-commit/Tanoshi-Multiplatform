package tanoshi.multiplatform.shared.extension

import java.io.File
import java.io.FileInputStream

actual class ExtensionManager {
    
    var dir : File = File( System.getProperty( "user.dir" ) )
    
    actual val extensionLoader: ExtensionLoader = ExtensionLoader()
    
    actual fun install(extensionId: String, file: File) {
        install( extensionId , file.inputStream() )
    }
    
    actual fun install(extensionId: String, fileInputStream: FileInputStream) {
        if ( ! dir.isDirectory ) dir.mkdirs()
        File( dir , extensionId ).outputStream().use { output ->
            val buffer = ByteArray( 1024 )
            var len = 0
            while ( fileInputStream.read( buffer ).also { len = it } > 0 ) {
                output.write( buffer , 0 , len )
            }
        }
        
    }

    actual fun uninstall(extensionId: String) {
        File( dir , extensionId ).delete()
    }

}