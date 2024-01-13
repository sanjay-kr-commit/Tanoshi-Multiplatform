package tanoshi.multiplatform.shared.extension

import tanoshi.multiplatform.common.exception.extensionManager.IllegalDependenciesFoundException
import tanoshi.multiplatform.common.util.restrictedClasses
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipFile

actual class ExtensionManager {

    var dir : File = File( System.getProperty( "user.dir" ) )
    
    actual val extensionLoader: ExtensionLoader = ExtensionLoader()

    var mappedRestrictedDependencies = HashMap<String,HashSet<String>>()

    private val String.isRestricted : Boolean
        get() = restrictedClasses.contains( this )


    private fun checkExtensionForRestrictedDepencies(
        extension : String
    ) = ZipFile( extension ).use { zip ->
        zip.entries().asSequence().forEach { zipEntry ->
            if ( zipEntry.name.endsWith( ".class" ) ) {
                val classBuffer = zip.getInputStream( zipEntry ).bufferedReader().use{ it.readText() }
                checkRestrictedDependencies( zipEntry.name.replace( "/" , "." ) , classBuffer )
            }
        }
    }

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
    
    actual fun install(extensionId: String, file: File) {
        install( extensionId, file.inputStream() ) ;
    }
    
    actual fun install(extensionId: String, fileInputStream: FileInputStream) {
        mappedRestrictedDependencies = HashMap()
        // clean up
        File( dir , "temp" ).deleteRecursively()
        // write to temporary directory
        val extensionFile = File( dir , "temp/$extensionId/extension.jar" )
        extensionFile.absoluteFile.parentFile.let {
            if ( !it.isDirectory ) it.mkdirs()
        }
        extensionFile.outputStream().use { extension ->
            val buffer : ByteArray = ByteArray( 1024 )
            var len : Int
            while ( fileInputStream.read( buffer ).also { len = it } > 0 ) {
                extension.write( buffer , 0 , len )
            }
        }

        // inspect file for certain dependency
        checkExtensionForRestrictedDepencies( extensionFile.absolutePath )
        if ( mappedRestrictedDependencies.isNotEmpty() ) {
            val buffer = StringBuilder()
            for ( ( className , dependencies ) in mappedRestrictedDependencies ) buffer.append(
                """
                    |class : $className
                    |Restricted Dependencies : $dependencies
                    |
                """.trimMargin()
            )
            throw IllegalDependenciesFoundException( buffer.toString() )
        }

        extensionFile.absoluteFile.parentFile.renameTo(
            File( dir , extensionId )
        )
    }

    actual fun uninstall(extensionId: String) {
        File( dir , extensionId ).deleteRecursively()
    }

}