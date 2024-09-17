package tanoshi.extensions.anime

import tanoshi.multiplatform.common.extension.annotations.Variable

data class Config(
    @Variable( "Dub" , "enableDub" )
    var isDubEnabled : Boolean = false
)