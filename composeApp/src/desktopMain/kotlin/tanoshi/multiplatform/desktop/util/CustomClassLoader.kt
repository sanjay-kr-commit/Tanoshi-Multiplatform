package tanoshi.multiplatform.desktop.util

import java.net.URL
import java.net.URLClassLoader
import java.util.zip.ZipFile

class CustomClassLoader(
    private val urls : Array<URL> ,
    parentClassLoader : ClassLoader = CustomClassLoader::class.java.classLoader ,
    restrictedClassName : List<String> = listOf()
) : URLClassLoader( urls , parentClassLoader ) {

    val mappedRestrictedDependencies = HashMap<String,HashSet<String>>()

    private val restrictedClass : HashSet<String> = HashSet<String>().apply {
        addAll( restrictedClassName )
    }

    private val String.isRestricted : Boolean
        get() = restrictedClass.contains( this )

    init {
        checkDependencies()
    }

    private fun checkDependencies() {
        urls.forEach { url ->
            ZipFile( url.toString().replace( "file:" , "" ) ).use { zip ->
                zip.entries().asSequence().forEach { zipEntry ->
                    if ( zipEntry.name.endsWith( ".class" ) ) {
                        val classBuffer = zip.getInputStream( zipEntry ).bufferedReader().use{ it.readText() }
                        checkRestrictedDependencies( zipEntry.name.replace( "/" , "." ) , classBuffer )
                    }
                }
            }
        }
    }

    private fun checkRestrictedDependencies( parentClass : String , classBuffer : String ) {
        Regex( "L[0-9a-zA-Z/]*;" ).findAll( classBuffer )
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
    }

    override fun loadClass(name: String?): Class<*> {
        return super.loadClass(name)
    }

}