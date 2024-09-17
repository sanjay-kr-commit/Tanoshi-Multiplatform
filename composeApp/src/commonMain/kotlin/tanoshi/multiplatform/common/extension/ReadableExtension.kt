package tanoshi.multiplatform.common.extension

import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.SelectableMenu

interface ReadableExtension : Extension<ReadableEntry> {

    override val name : String

    override val domainsList : SelectableMenu<String>

    override val language : String

    override fun search( name : String , index : Int ) : List<ReadableEntry>

    fun fetchDetail( entry : ReadableEntry )

    fun fetchPlayableContentList( entry : ReadableEntry )

    fun fetchPlayableMedia( entry : ReadableContent ) : List<ReadableMedia>
    
}