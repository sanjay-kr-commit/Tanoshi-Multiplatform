package tanoshi.multiplatform.common.extension

import kotlinx.serialization.Serializable

@Serializable
data class ReadableContent(
    var name : String? = null ,
    var url : String? = null ,
    val lanuage : String? = null
)