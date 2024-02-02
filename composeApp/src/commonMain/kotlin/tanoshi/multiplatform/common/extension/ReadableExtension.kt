package tanoshi.multiplatform.common.extension

import tanoshi.multiplatform.common.extension.interfaces.Extension
import tanoshi.multiplatform.common.util.SelectableMenu

interface ReadableExtension : Extension {
    
    val name : String

    val domainsList : SelectableMenu<String>

    val language : String

    fun search( name : String , index : Int ) : List<ReadableEntry>

    fun fetchDetail( entry : ReadableEntry )

    fun fetchPlayableContentList( entry : ReadableEntry )

    fun fetchPlayableMedia( entry : ReadableContent ) : List<ReadableMedia>
    
}