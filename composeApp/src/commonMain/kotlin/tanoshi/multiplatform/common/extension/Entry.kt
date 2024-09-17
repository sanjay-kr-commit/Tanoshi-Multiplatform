package tanoshi.multiplatform.common.extension

open class Entry < ContentType > {
    var name : String? = null
    var language : String? = null
    var url : String? = null
    var coverArt : String? = null
    var content : List<ContentType>? = null
    var otherData : HashMap<String,String>? = null
}