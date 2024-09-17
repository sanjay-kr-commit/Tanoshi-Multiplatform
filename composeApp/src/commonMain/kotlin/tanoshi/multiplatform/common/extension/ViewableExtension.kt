package tanoshi.multiplatform.common.extension

import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.SelectableMenu

interface ViewableExtension : Extension<ViewableEntry> {
    
    override val name : String

    override val domainsList : SelectableMenu<String>

    override val language : String

    override fun search( name : String , index : Int ) : List<ViewableEntry>

    fun fetchDetail( entry : ViewableEntry )

    fun fetchPlayableContentList( entry : ViewableEntry )

    fun fetchPlayableMedia( entry : ViewableContent ) : List<ViewableMedia>
    
}