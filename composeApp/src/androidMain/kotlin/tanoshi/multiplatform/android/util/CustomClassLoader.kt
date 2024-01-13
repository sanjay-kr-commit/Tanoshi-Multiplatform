package tanoshi.multiplatform.android.util

import java.io.File
import dalvik.system.PathClassLoader

class CustomClassLoader(
    private val dexPath : File ,
    private val parentClassLoader : ClassLoader ,
    restrictedClassName : List<String> = listOf()
) : PathClassLoader( dexPath.absolutePath , parentClassLoader ) {

    val mappedRestrictedDependencies = HashMap<String,HashSet<String>>()

    private val restrictedClass : HashSet<String> = HashSet<String>().apply {
        addAll( restrictedClassName )
    }

    private val String.isRestricted : Boolean
        get() = restrictedClass.contains( this )

    init {
        checkDependencies()
    }

    private fun checkDependencies() = checkRestrictedDependencies( dexPath.name , dexPath.readText() )

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