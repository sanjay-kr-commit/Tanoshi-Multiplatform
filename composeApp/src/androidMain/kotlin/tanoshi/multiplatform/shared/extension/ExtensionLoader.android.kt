package tanoshi.multiplatform.shared.extension

import android.content.Context
import tanoshi.multiplatform.common.extension.interfaces.Extension
import java.io.File
import java.lang.Exception
import dalvik.system.PathClassLoader

actual class ExtensionLoader {
    
    lateinit var applicationContext : Context
    
    actual val loadedExtensionClasses : HashMap< String , Extension > = HashMap()

    actual fun loadTanoshiExtension( vararg tanoshiExtensionFile : String ) {
        if ( !::applicationContext.isInitialized ) throw UninitializedPropertyAccessException( "application context not initialised" )
        tanoshiExtensionFile.forEach { tanoshiFile ->
            loadTanoshiFile( tanoshiFile )
        }
    }
    
    private fun loadTanoshiFile( tanoshiExtensionFile: String ) = applicationContext.run {
        val dexFiles : List<String> = getDir( "extension/$tanoshiExtensionFile" , Context.MODE_PRIVATE ).listFiles().run {
            val listOfDexFile = ArrayList<String>()
            forEach { file ->
                val fileString = file.toString()
                if ( fileString.endsWith( ".dex" ) ) listOfDexFile.add(
                    if ( fileString.contains( "/" ) ) fileString.substring( fileString.lastIndexOf( "/" )+1 )
                    else fileString
                )
            }
            listOfDexFile
        }
        dexFiles.forEach { dexName ->
            try {
                loadDexFile( tanoshiExtensionFile , dexName )
            } catch ( _ : Exception ) {
            } catch ( _ : Error ) { }
        }
    }
    
    private fun loadDexFile( tanoshiExtensionFile: String , dexFileName : String ) = applicationContext.run {
        val file = File( getDir( "extension/$tanoshiExtensionFile" , Context.MODE_PRIVATE ) , dexFileName )
        val classLoader = PathClassLoader( file.absolutePath , classLoader )
        val classNameList : List<String> = Regex( "L[a-zA-Z0-9/]*;" ).findAll( file.readText() ).run {
            val nameList = ArrayList<String>( count() )
            forEach {  lClassPath ->
                nameList.add(
                    lClassPath.value.substring( 1 , lClassPath.value.length-1 ).replace( "/" , "." )
                )
            }
            nameList
        }
        classNameList.forEach { className -> 
            try {
                val loadedClass : Class<*> = classLoader.loadClass( className )
                val obj : Any = loadedClass.getDeclaredConstructor().newInstance()
                if ( obj is Extension ) loadedExtensionClasses[className] = obj as Extension
            } catch ( _ : Exception ) {
            } catch ( _ : Error ) {
            }
        }
    }
    
}