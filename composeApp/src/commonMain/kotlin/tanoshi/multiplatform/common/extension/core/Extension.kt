package tanoshi.multiplatform.common.extension.core

import tanoshi.multiplatform.common.extension.Entry
import tanoshi.multiplatform.common.util.SelectableMenu

interface Extension <E:Entry<*>> {
    val name : String

    val domainsList : SelectableMenu<String>

    val language : String

    fun search( name : String , index : Int ) : List<E>

}