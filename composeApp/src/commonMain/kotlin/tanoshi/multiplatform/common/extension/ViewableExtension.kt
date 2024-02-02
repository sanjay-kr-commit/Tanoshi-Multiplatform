package tanoshi.multiplatform.common.extension

import tanoshi.multiplatform.common.extension.interfaces.Extension
import tanoshi.multiplatform.common.util.SelectableMenu

interface ViewableExtension : Extension {
    
    val name : String

    val domainsList : SelectableMenu<String>

    val language : String

    fun search( name : String , index : Int ) : List<ViewableEntry>

    fun fetchDetail( entry : ViewableEntry )

    fun fetchPlayableContentList( entry : ViewableEntry )

    fun fetchPlayableMedia( entry : ViewableContent ) : List<ViewableMedia>
    
}