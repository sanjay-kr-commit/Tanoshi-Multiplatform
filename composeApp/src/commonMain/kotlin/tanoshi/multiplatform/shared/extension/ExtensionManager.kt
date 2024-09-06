package tanoshi.multiplatform.shared.extension

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.logger.Logger
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

expect class ExtensionManager {
    
    val extensionLoader : ExtensionLoader

    fun install( file : File )

    fun loadExtensions()

    fun unloadExtensions()

    val Extension.icon : @Composable () -> Unit

    var dir : File

    var cacheDir : File

}

val ExtensionManager.packageList : List<String>
    get() = ArrayList<String>().apply {
        dir.listFiles()?.let { addAll( it.map { file ->
            file.name.toString()
        } ) }
    }

fun ExtensionManager.uninstall(extensionId: String) {
    val extensionDir = File( dir , extensionId )
    extensionDir.walk().forEach { it.setWritable( true ) }
    if ( extensionDir.isDirectory ) extensionDir.deleteRecursively()
    if ( extensionDir.isFile ) extensionDir.delete()
}

fun ExtensionManager.extractExtension( file: File , logger: Logger ) : File? {

    val cacheExtensionDir = File( cacheDir , "UnknownExtension" )

    cacheExtensionDir.setWritable( true )
    if ( cacheExtensionDir.isDirectory ) cacheExtensionDir.deleteRecursively()
    if ( cacheExtensionDir.isFile ) cacheExtensionDir.delete()
    if ( !cacheExtensionDir.isDirectory ) cacheExtensionDir.mkdirs()

    val extensionFile = File( cacheExtensionDir , "source.tanoshi" )

    file.copyTo( extensionFile )

    ZipInputStream( BufferedInputStream( FileInputStream( extensionFile ) ) ).use { extension ->
        var zipEntry : ZipEntry? = extension.nextEntry
        while (zipEntry != null) {

            val isDirectory = zipEntry!!.name.endsWith(File.separator)

            if ( isDirectory ) File( cacheExtensionDir , zipEntry.name ).mkdirs()
            else extension.copyTo( File( cacheExtensionDir , zipEntry.name ).outputStream() )

            zipEntry = extension.nextEntry
        }
    }

    var extensionId : String = ""

    File( cacheExtensionDir , "META-INF/MANIFEST.MF" ).let { manifest ->
        if ( !manifest.isFile ) {
            logger log {
                ERROR
                title = "Manifest File Not Found"
                "Cannot install extension manifest file not found"
            }
            cacheExtensionDir.deleteRecursively()
            return null
        }
        for ( line in manifest.readLines() ) {
            if ( line.startsWith( "extension-namespace" ) ) {
                extensionId = line
                    .trim()
                    .removePrefix( "extension-namespace" )
                    .trim()
                    .removePrefix( ":" )
                    .trim()
                break
            }
        }
        if (extensionId.isEmpty()) {
            logger log {
                ERROR
                title = "Namespace not found in manifest file"
                """
                    !!!! Developer Information !!!!
                    Add Below line to gradle project to add namespace of extension package
                    "tasks.withType( Jar::class.java ) {
                        manifest {
                            attributes["extension-namespace"] = "namespace"
                        }
                    }"
                """.trimIndent()
            }
            cacheExtensionDir.deleteRecursively()
            return null
        }
    }

    val extensionDir = File( dir , extensionId )
    extensionDir.setWritable( true )
    if ( extensionDir.isDirectory ) {
        extensionDir.walk().forEach { it.setWritable( true ) }
        extensionDir.deleteRecursively()
    }
    if ( extensionDir.isFile ) extensionDir.delete()
    if ( !extensionDir.isDirectory ) extensionDir.mkdirs()

    cacheExtensionDir.copyRecursively( extensionDir , overwrite = true )

    return extensionDir
}