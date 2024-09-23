package tanoshi.multiplatform.common.extension

import kotlinx.serialization.Serializable

@Serializable
data class PlayableMedia(
    var mediaUrl : String? = null ,
    var mediaLang : String? = null ,
    var subtitleUrl : String? = null ,
    var subtitleLang : String? = null ,
    var quality : String? = null ,
)