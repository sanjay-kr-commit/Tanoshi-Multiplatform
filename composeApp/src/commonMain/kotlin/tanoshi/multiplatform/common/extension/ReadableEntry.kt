package tanoshi.multiplatform.common.extension

data class ReadableEntry(
    var name : String? = null ,
    var language : String? = null ,
    var content : List<ReadableContent>? = null ,
    var otherData : HashMap<String,String>? = null ,
    var url : String? = null
)