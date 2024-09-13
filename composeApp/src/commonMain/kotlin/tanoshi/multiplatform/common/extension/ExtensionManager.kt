package tanoshi.multiplatform.common.extension

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.core.SharedDependencies
import tanoshi.multiplatform.common.extension.enum.SharedDependencyFields
import tanoshi.multiplatform.common.util.Manifest.Companion.toManifest
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.toFile
import tanoshi.multiplatform.shared.extension.ExtensionManager
import tanoshi.multiplatform.shared.util.loadImageBitmap
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun ExtensionManager.reloadExtension() {
    unloadExtensions()
    loadExtensions()
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

fun ExtensionManager.extractExtension(file: File, logger: Logger) : File? {

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
        extensionId = manifest.toManifest.extensionNamespace?: ""
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

@Composable
fun ExtensionManager.extensionIcon( packageName :String ) {
    extensionIconPath[packageName]?.let {
        val image = remember { loadImageBitmap( it.toFile ) }
        image?.let { imageBitmap ->
            Image( imageBitmap , "", contentScale = ContentScale.Crop , modifier = Modifier.clip( RoundedCornerShape( 10.dp ) ) )
        } ?: run {
            Icon( Icons.Default.Error , "" )
        }
    } ?: run {
        Icon( Icons.Default.BrokenImage , "" )
    }
}

fun createExtensionPermissionFile(extension: Extension, file: File) {
    if ( extension is SharedDependencies ) {
        file.bufferedWriter().use { permission ->
            SharedDependencyFields.entries.forEach { entry ->
                permission.write( "${entry.name}:${entry.defaultValue}\n" )
            }
        }
    }
}