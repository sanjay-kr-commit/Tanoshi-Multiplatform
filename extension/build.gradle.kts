import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {

    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation( project( ":composeApp" ) )
            implementation( libs.jsoup )
            implementation( libs.okhttp )
        }
    }
}

tasks.withType( Jar::class.java ) {
    manifest {
        attributes["extension-namespace"] = "hello.world"
        attributes["extension-version"] = "0.0.1"
        attributes["extension-version-code"] = 1
    }
}

tasks.register<Zip>("buildExtension") {
    Thread.sleep( 100 )
    if( !projectDir.child( "build" ).also { println( it ) }.isDirectory ) {
        println( "run build first" )
        return@register
    }
    val localProperties: Properties = Properties().apply {
        rootProject.file("local.properties").reader().use(::load)
    }
    val sdkPath: String = (localProperties["sdk.dir"] ?: "").toString()
    if (sdkPath == "") {
        println("Android SDK Not Found : $sdkPath")
        return@register
    }
    val dexCompilerPath =
    sdkPath.toFile.child("build-tools").listFiles()?.toList()?.sorted().let { it?.get(it.size - 1) }
    ?.child("d8")?.absolutePath.toString()
    val libPath = projectDir.child("build").child("libs").absoluteFile.toString()
    val jarFile = libPath.toFile.child("${project.name}-jvm.jar").absoluteFile.toString()
    println(dexCompilerPath)
    println(libPath)
    println(jarFile)
    val process =
    ProcessBuilder("/bin/bash" , "-c" , "$dexCompilerPath $jarFile --output $libPath".also { println(it) }).inheritIO()
    .start()
    process.waitFor()

    Thread.sleep( 100 )

    val files = libPath.toFile.listFiles()?.toList()
    val dexFiles = files?.filter { it.name.endsWith( ".dex" ) }

    println( files )
    println(dexFiles)

    if( "$jarFile.zip".toFile.isFile ) "$jarFile.zip".toFile.delete()

        ZipOutputStream( BufferedOutputStream( FileOutputStream( "$jarFile.zip" ) ) ).use { out ->
            ZipFile( jarFile ).use { jar ->
                jar.entries().asSequence().forEach { entry ->
                    out.putNextEntry(entry)
                    jar.getInputStream(entry).buffered().use { origin ->
                        origin.copyTo(out,  1024)
                    }
                }
            }
            dexFiles?.forEach { dexFile ->
                BufferedInputStream( FileInputStream( dexFile ) ).use { origin ->
                    val entry = ZipEntry(dexFile.toString().substring(dexFile.toString().lastIndexOf("/")))
                    out.putNextEntry(entry)
                    origin.copyTo(out, 1024)
                }
            }
        }

        if ( "$jarFile.tanoshi".toFile.isFile ) "$jarFile.tanoshi".toFile.delete()
            "$jarFile.zip".toFile.renameTo( "$jarFile.tanoshi".toFile )

}

fun File.child( name : String ) : File = File( this , name )
val String.toFile : File
get() = File( this )
