package tanoshi.multiplatform.shared.extension

import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.gson.Gson
import tanoshi.multiplatform.common.extension.annotations.IconName
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.child
import tanoshi.multiplatform.common.util.logger.Logger
import java.io.File
import java.lang.ClassCastException
import java.net.URL
import java.net.URLClassLoader

actual class ExtensionManager {

    lateinit var logger: Logger

    actual val extensionIconPath : HashMap<String,String> = hashMapOf()

    actual var dir : File = File( System.getProperty( "user.dir" ) , ".tanoshi/extensions" )

    actual var cacheDir : File = File( System.getProperty( "user.dir" ) , ".tanoshi/cache" )

    actual val extensionLoader: ExtensionLoader = ExtensionLoader()

    private val gson = Gson()

    actual fun install(file: File) {
        extractExtension(file, logger)?.let { extensionDir ->

            extensionDir.child( "source.tanoshi" ).copyTo( extensionDir.child( "source.jar" ) )

            val classDependencies = ArrayList<Pair<String,List<String>>>()
            val extensionClasses = ArrayList<String>()
            val extensionIcon = HashMap<String,String>()

            val classPath = extensionDir.walk().filter {
                it.absolutePath.endsWith( ".class" )
            }.map {
                it.absolutePath.removePrefix( extensionDir.absolutePath )
                    .removeSuffix( ".class" )
                    .removePrefix( "/" )
                    .replace( "/" , "." )
            }.toList()

            val classLoader = URLClassLoader(
                arrayOf( extensionDir.child( "source.jar" ).absolutePath.url ) ,
                this.javaClass.classLoader
            )

            classPath.forEach { className ->
                try {
                    val loadedClass: Class<*> = classLoader.loadClass(className)
                    val obj: Any = loadedClass.getDeclaredConstructor().newInstance()
                    if (obj !is Extension) throw ClassCastException( "$className is not an extension" )
                    extensionClasses.add( className )
                    obj::class.java.annotations.filterIsInstance<IconName>().firstOrNull()?.let { iconName ->
                        if ( extensionDir.child( iconName.icon ).isFile ) {
                            extensionIcon += className to iconName.icon
                        }
                    }
                    addDependencyTree( className , classPath , extensionDir , classDependencies )
                } catch ( e : Exception ) {
                    logger log {
                        ERROR
                        title = "checking $className"
                        e.stackTraceToString()
                    }
                }
            }

            extensionDir.child( "extensionClasses.json" )
                .writeText(
                    gson.toJson( extensionClasses )
                )

            extensionDir.child( "extensionIcon.json" )
                .writeText(
                    gson.toJson( extensionIcon )
                )

            extensionDir.child( "extensionClassDependencyTree.json" )
                .writeText(
                    gson.toJson( classDependencies )
                )

        }
    }

    private fun addDependencyTree(
        className : String,
        classList : List<String>,
        extensionDir : File,
        classDependencies :ArrayList<Pair<String,List<String>>> ,
        visited : HashSet<String> = hashSetOf()
    ) {
        if ( visited.contains( className ) ) return
        visited.add( className )
        val dependencies = extensionDir.child( className.replace( "." , "/" ) + ".class" ).readText()
            .let { buffer ->
                Regex( "L[a-zA-Z0-9/]*;" ).findAll( buffer )
            }.map { compClassName ->
                compClassName.value
                    .replace( "/" , "." )
                    .let { name ->
                        name.substring( 1 , name.length-1 )
                    }
            }.filter { dependencyClassName ->
                classList.contains( dependencyClassName )
            }.toList()
        classDependencies += className to dependencies
        dependencies.forEach { dependencyClassName ->
            addDependencyTree( dependencyClassName , classList , extensionDir , classDependencies , visited )
        }
    }

    @Suppress("UNCHECKED_CAST")
    actual fun loadExtensions() {
        dir.listFiles()?.forEach { extensionDir ->
            val path = extensionDir.absolutePath
            try {
                val extensionClasses: ArrayList<String> = gson.fromJson(
                    extensionDir.child("extensionClasses.json").readText(),
                    ArrayList::class.java
                ) as ArrayList<String>
                extensionLoader.loadTanoshiExtension(
                    extensionDir.child( "source.jar" ) ,
                    extensionClasses
                )
            } catch ( e : Exception ) {
                logger log {
                    title = "loading extension ${extensionDir.name}"
                    e.stackTraceToString()
                }
            }

            try {

                val extensionIcon : HashMap<String,String> = gson.fromJson(
                    extensionDir.child( "extensionIcon.json" ).readText() ,
                    HashMap::class.java
                ) as HashMap<String,String>

                for ( (name,icon) in extensionIcon ) {
                    extensionIconPath += name to extensionDir.child( icon ).absolutePath
                }

            } catch ( e : Exception ){
                println( e.stackTraceToString() )
            }

        }
    }

    actual fun unloadExtensions() {
    }

    private val String.url : URL
        get() = File( this ).toURI().toURL()

}