package tanoshi.multiplatform.common.extension

import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.util.SelectableMenu

interface PlayableExtension : Extension<PlayableEntry> {

    override val name : String

    override val domainsList : SelectableMenu<String>

    override val language : String
    
    override fun search( name : String , index : Int ) : List<PlayableEntry>
    
    fun fetchDetail( entry : PlayableEntry )
    
    fun fetchPlayableContentList( entry : PlayableEntry )
    
    fun fetchPlayableMedia( entry : PlayableContent ) : List<PlayableMedia>
    
}