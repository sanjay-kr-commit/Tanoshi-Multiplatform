package tanoshi.multiplatform.shared.extension

import androidx.compose.runtime.Composable
import com.google.gson.Gson
import dalvik.system.DexClassLoader
import tanoshi.multiplatform.common.extension.annotations.IconName
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.extractExtension
import tanoshi.multiplatform.common.util.child
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.toFile
import java.io.File
import java.lang.ClassCastException

actual class ExtensionManager {

    lateinit var logger: Logger

    actual val extensionIconPath : HashMap<String,String> = hashMapOf()

    private lateinit var _classLoader_ : ClassLoader
    var classLoader : ClassLoader
        set(value) {
            logger log {
                title = "Initialised Class Loader"
                """
                    Classloader attached to Extension Manager & Loader
                """.trimIndent()
            }
            _classLoader_ = value
            extensionLoader.appClassLoader = value
        }
        get() = _classLoader_

    actual var dir: File = File("")
    actual var cacheDir : File = File( "" )

    actual val extensionLoader: ExtensionLoader = ExtensionLoader()

    private val gson = Gson()

    actual fun install( file: File ) {
        extractExtension(file, logger)?.let { extensionDir ->

            extensionDir.setExecutable(true)

            val dexList = extensionDir.walk()
                .filter { file ->
                    file.absolutePath.endsWith( ".dex" )
                }.toList()

            val extensionClassesDex : HashMap<String,List<String>> = hashMapOf()
            val extensionIcon = HashMap<String,String>()
            val classDependencies = ArrayList<Pair<String,List<String>>>()

            dexList.forEach { dexFile ->
                dexFile.setReadOnly()
                val classList = dexFile.readText().let { buffer ->
                    Regex( "L[a-zA-Z0-9/]*;" ).findAll( buffer )
                }.map { compClassName ->
                    compClassName.value
                        .replace( "/" , "." )
                        .let { name ->
                            name.substring( 1 , name.length-1 )
                        }
                }.toList()

                val extensionClasses = ArrayList<String>() ;
                val classLoader = DexClassLoader( dexFile.absolutePath , null , null , _classLoader_ )

                classList.forEach { className ->
                    try {
                        val loadedClass: Class<*> = classLoader.loadClass(className)
                        val obj: Any = loadedClass.getDeclaredConstructor().newInstance()
                        if (obj !is Extension) throw ClassCastException( "$className is not an extension" )
                        if ( extensionClasses.contains( className ) ) throw Exception( "Duplicate Class" )
                        extensionClasses.add(className)
                        obj::class.java.annotations.filterIsInstance<IconName>().firstOrNull()
                            ?.let { iconName ->
                                if (extensionDir.child(iconName.icon).isFile) {
                                    extensionIcon += className to iconName.icon
                                }
                            }
                        addDependencyTree( className , classList , classDependencies , classLoader )
                    } catch ( _ : Exception ) {}
                }

                // add all the classes associated to dex file
                extensionClassesDex += dexFile.absolutePath to extensionClasses

            }

            extensionDir.child( "extensionClassesDex.json" )
                .writeText(
                    gson.toJson( extensionClassesDex )
                )

            extensionDir.child( "extensionIcon.json" )
                .writeText(
                    gson.toJson( extensionIcon )
                )

            extensionDir.child( "extensionClassDependencyTree.json" )
                .writeText(
                    gson.toJson( classDependencies )
                )

            extensionDir.setWritable(false)
            extensionDir.setReadOnly()
            extensionDir.setExecutable(true)

        }

    }

    private fun addDependencyTree(
        className : String,
        classList : List<String>,
        classDependencies :ArrayList<Pair<String,List<String>>> ,
        classLoader: DexClassLoader ,
        visited : HashSet<String> = hashSetOf()
    ) {
        if ( visited.contains( className ) ) return ;
        visited += className
        val dependencies = classLoader.loadClass( className ).classes.map {
            it.name
        }
        logger log  {
            title = className
            dependencies.toString()
        }
        classDependencies += className to dependencies
        dependencies.forEach { dependencyClassName ->
            addDependencyTree( dependencyClassName , classList , classDependencies , classLoader , visited )
        }
    }

    actual fun loadExtensions() {
        dir.listFiles()?.forEach { extensionDir ->
            val path = extensionDir.absolutePath
            try {
                val extensionClasses: HashMap<String,List<String>> = gson.fromJson(
                    extensionDir.child("extensionClassesDex.json").readText(),
                    HashMap::class.java
                ) as HashMap<String,List<String>>
                extensionClasses.forEach {
                    extensionLoader.loadTanoshiExtension(
                        it.key.toFile ,
                        it.value
                    )
                }
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
                logger log {
                    ERROR
                    title = "Loading Icon Path"
                    e.stackTraceToString()
                }
            }

        }
    }

    actual fun unloadExtensions() {
    }

}