package tanoshi.multiplatform.common.extension

data class PlayableEntry(
    var name : String? = null ,
    var language : String? = null ,
    var content : List<PlayableContent>? = null ,
    var otherData : HashMap<String,String>? = null ,
    var coverArt : String? = null ,
    var url : String? = null
)