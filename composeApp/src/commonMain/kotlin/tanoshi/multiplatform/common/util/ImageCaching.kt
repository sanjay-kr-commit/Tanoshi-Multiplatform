package tanoshi.multiplatform.common.util

import java.io.File
import java.net.URL

object ImageCaching {

    fun loadImage(
        url : String ,
        dir : File
    ) : File {
        val hash = dir.child( "imageCache" ).also { it.mkdirs() }.child( url.hash256 )
        if ( !hash.isFile ) {
            hash.createNewFile()
            @Suppress("DEPRECATION")
            URL( url ).let {
                it.openConnection()
                it.openStream().use { ins ->
                    hash.outputStream().use { os ->
                        ins.copyTo( os )
                    }
                }
            }
        }
        return hash
    }

}