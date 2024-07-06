package tanoshi.multiplatform.shared.extension

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import tanoshi.multiplatform.common.exception.extensionManager.IllegalDependenciesFoundException
import tanoshi.multiplatform.common.extension.annotations.IconName
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.restrictedClasses
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipFile

actual class ExtensionManager {

    lateinit var logger: Logger

    var dir : File = File( System.getProperty( "user.dir" ) , "tanoshi/extensions" )
    
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
        logger log {
            DEBUG
            title = "Installing $extensionId"
            title
        }
        mappedRestrictedDependencies = HashMap()
        // clean up
        File( dir , "temp" ).deleteRecursively()
        // write to temporary directory
        val extensionFile = File( dir , "temp/$extensionId/extension.jar" )
        extensionFile.absoluteFile.parentFile.let {
            if ( !it.isDirectory ) it.mkdirs()
        }
        val extensionResource = File( dir , "temp/$extensionId/" )
        extensionFile.outputStream().use { extension ->
            val buffer : ByteArray = ByteArray( 1024 )
            var len : Int
            while ( fileInputStream.read( buffer ).also { len = it } > 0 ) {
                extension.write( buffer , 0 , len )
            }
        }

        // unpack resources
        ZipFile( extensionFile ).use { loadedExtension ->
            for ( zipPath in loadedExtension.entries().asSequence() ) {
                if ( zipPath.isDirectory ) continue
                val zipPathString = zipPath.toString()
                if ( zipPathString.endsWith( ".class" ) || zipPathString.endsWith( ".dex" ) || zipPathString.endsWith( ".kotlin_module" ) ) continue
                var i = 0
                while ( i < zipPathString.length && zipPathString[i] == '\\' ) i++
                while ( i < zipPathString.length && zipPathString[i] == '/' ) i++
                val exportedFile = File( dir , "temp/$extensionId/${zipPathString.substring( i )}" )
                if ( exportedFile.isFile ) exportedFile.delete()
                if ( !exportedFile.parentFile.isDirectory ) exportedFile.parentFile.mkdirs()
                exportedFile.outputStream().buffered().use { exportAddress ->
                    loadedExtension.getInputStream( zipPath ).copyTo(
                        exportAddress , 1024
                    )
                }
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
        File( dir , "temp" ).deleteRecursively()
        loadExtensions()
    }

    actual fun uninstall(extensionId: String) {
        logger log {
            DEBUG
            title = "Uninstalling $extensionId"
            title
        }
        File( dir , extensionId ).deleteRecursively()
        extensionLoader.loadedExtensionClasses.clear()
        loadExtensions()
    }

    init {
        if ( !dir.isDirectory ) dir.mkdirs()
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
            extensionId.listFiles()?.forEach { jar ->
                extensionLoader.loadTanoshiExtension( jar.absolutePath )
            }
        }
    }

        actual val Extension.icon : @Composable () -> Unit
            get() {
                return {
                    Text( "TODO" )
                }
            }

}