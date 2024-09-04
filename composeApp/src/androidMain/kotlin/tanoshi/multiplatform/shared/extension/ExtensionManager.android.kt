package tanoshi.multiplatform.shared.extension

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.exception.extensionManager.IllegalDependenciesFoundException
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.restrictedClasses
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


actual class ExtensionManager {

    actual var dir : File = File("")

    lateinit var logger: Logger

    actual val extensionLoader: ExtensionLoader = ExtensionLoader()
    
    var mappedRestrictedDependencies = HashMap<String,HashSet<String>>()

    private val String.isRestricted : Boolean
        get() = restrictedClasses.contains( this )


    private fun checkExtensionForRestrictedDepencies(
       pathToDex : File
    ) = checkRestrictedDependencies( pathToDex.toString() , pathToDex.readText() )

    private fun checkRestrictedDependencies( parentClass : String , classBuffer : String ) = Regex( "L[0-9a-zA-Z/]*;" ).findAll( classBuffer )
        .map { it.value.replace( "/" , "." ).replace( ";" , "" ).substring( 1 ) }
        .let { dependencyClasses ->
            dependencyClasses.forEach { dependencyName ->
                if ( dependencyName.isRestricted ) {
                    mappedRestrictedDependencies[parentClass]?.add( dependencyName ) ?: run {
                        mappedRestrictedDependencies[parentClass] = hashSetOf( dependencyName )
                    }
                }
            }
        }

    actual fun install(extensionId: String, file: File ) {
        install( extensionId , file.inputStream() )
    }

    actual fun install(extensionId: String, fileInputStream: FileInputStream) : Unit {

        logger log {
            DEBUG
            title = "Installing $extensionId"
            title
        }

        // clean up
        mappedRestrictedDependencies = HashMap()

        File( dir , "temp" ).deleteRecursively()

        // extract extension in temporary directory
        val extensionDir = File( dir , "temp" ).apply {
            if ( !isDirectory ) mkdirs()
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

        ZipInputStream( BufferedInputStream( FileInputStream( extensionFile ) ) ).use { extension ->
            var zipEntry : ZipEntry? = extension.nextEntry
            while (zipEntry != null) {

                val isDirectory = zipEntry.name.endsWith(File.separator)

                if ( isDirectory ) File( extensionDir , zipEntry.name ).mkdirs()
                else extension.copyTo( File( extensionDir , zipEntry.name ).outputStream() )

                zipEntry = extension.nextEntry
            }
        }

        // check for restricted dependency
        extensionDir.listFiles()?.forEach {
            if ( it.name.endsWith( ".dex" ) ) {
                checkExtensionForRestrictedDepencies( it )
            }
        }

        if ( mappedRestrictedDependencies.isNotEmpty() ) {
            val buffer = StringBuilder()
            for ( ( className , dependencies ) in mappedRestrictedDependencies ) buffer.append(
                """
                |class : $className
                |Restricted Dependencies : $dependencies
                |
                """.trimMargin()
            )
            throw IllegalDependenciesFoundException( "$buffer" )
        }

        extensionFile.delete()

        // installed
        extensionDir.renameTo(
            File( dir , extensionId )
        )

    }

    actual fun uninstall(extensionId: String) : Unit {
        logger log {
            DEBUG
            title = "Uninstalling $extensionId"
            title
        }
        File( dir , extensionId ).deleteRecursively()
        extensionLoader.loadedExtensionClasses.clear()
        loadExtensions()
    }

    actual fun unloadExtensions() {
        extensionLoader.loadedExtensionClasses.clear()
        extensionLoader.classList.clear()
    }

    actual fun loadExtensions() {
        logger log {
            DEBUG
            title = "Loading Extension"
            title
        }
        dir.listFiles()?.forEach { extensionId ->
            extensionLoader.loadTanoshiExtension( extensionId )
        }
    }

    actual val Extension.icon : @Composable () -> Unit
        get() {
            return {
                Text( "TODO" )
            }
        }
}