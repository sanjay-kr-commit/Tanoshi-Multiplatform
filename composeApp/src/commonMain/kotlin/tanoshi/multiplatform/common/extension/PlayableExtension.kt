package tanoshi.multiplatform.common.extension

import tanoshi.multiplatform.common.extension.interfaces.Extension
import tanoshi.multiplatform.common.util.SelectableMenu

interface PlayableExtension : Extension {
    
    val name : String
    
    val domainsList : SelectableMenu<String>
    
    val language : String
    
    fun search( name : String , index : Int ) : List<PlayableEntry>
    
    fun fetchDetail( entry : PlayableEntry )
    
    fun fetchPlayableContentList( entry : PlayableEntry )
    
    fun fetchPlayableMedia( entry : PlayableContent ) : List<PlayableMedia>
    
}