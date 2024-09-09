package tanoshi.multiplatform.common.util

import java.io.File

class Manifest {

    private val _properties_ = HashMap<String, String>()
    val properties: Map<String, String>
    get() = _properties_

    val manifestVersion : String?
        get() = _properties_["Manifest-Version"]
    val extensionNamespace : String?
        get() = _properties_["extension-namespace"]
    val extensionVersion : String?
        get() = _properties_["extension-version"]
    val extensionVersionCode : Int?
       get() = _properties_["extension-version-code"]?.toInt()

    companion object {

        val File.toManifest : Manifest
        get() = Manifest().apply {
            this@toManifest.readLines().forEach { line ->
                if ( line.contains( ":" ) && line.trim().isNotEmpty() ) line.split( ":" ).let { property ->
                    _properties_[property.first().trim()] = property[1].trim()
                }
            }
        }

    }

}