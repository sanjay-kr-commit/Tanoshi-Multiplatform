package tanoshi.multiplatform.shared.extension

import java.io.File

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ExtensionManager {

    val extensionLoader : ExtensionLoader

    val extensionIconPath : HashMap<String,String>

    fun install( file : File )

    fun loadExtensions()

    fun unloadExtensions()

    var dir : File

    var cacheDir : File

}