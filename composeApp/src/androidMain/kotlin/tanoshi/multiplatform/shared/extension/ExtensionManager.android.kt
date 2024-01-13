package tanoshi.multiplatform.shared.extension

import android.content.Context
import tanoshi.multiplatform.common.exception.extensionManager.IllegalDependenciesFoundException
import tanoshi.multiplatform.common.util.restrictedClasses
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

    actual fun install( extensionId: String , file: File ) {
        install( extensionId , file.inputStream() )
    }
    
    actual fun install(extensionId: String, fileInputStream: FileInputStream) : Unit = _applicationContext.run {
        // clean up
        mappedRestrictedDependencies = HashMap()
        getDir( "extension/temp" , Context.MODE_PRIVATE ).deleteRecursively()
        // extract extension in temporary directory
        val extensionDir = getDir( "extension/temp/$extensionId" , Context.MODE_PRIVATE ).also {
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

        // check for restricted dependency
        extensionDir.listFiles().forEach {
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

        // installed
        extensionDir.renameTo(
            getDir(
                "extension/$extensionId" , Context.MODE_PRIVATE
            )
        )

    }

    actual fun uninstall(extensionId: String) : Unit = _applicationContext.run {
        getDir( "extension/$extensionId" , Context.MODE_PRIVATE ).deleteRecursively()
    }

}