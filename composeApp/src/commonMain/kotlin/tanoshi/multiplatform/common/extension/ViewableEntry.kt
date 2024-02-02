package tanoshi.multiplatform.common.extension

data class ViewableEntry(
    var name : String? = null ,
    var language : String? = null ,
    var content : List<ViewableContent>? = null ,
    var otherData : HashMap<String,String>? = null ,
    var url : String? = null
)