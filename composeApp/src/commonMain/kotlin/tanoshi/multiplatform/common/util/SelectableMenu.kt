package tanoshi.multiplatform.common.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import tanoshi.multiplatform.common.exception.SelectableMenuIsImmutableException
import tanoshi.multiplatform.common.exception.SelectableMenuEntryNotFoundException

class SelectableMenu < T > (
    initialEntry : T , vararg entries : T
) {
    
    private val selectedElement : MutableState<T> = mutableStateOf( initialEntry )
    
    val activeElement : MutableState<T>
        get() = selectedElement
    val activeElementValue : T
        get() = selectedElement.value
    
    private var list : ArrayList<T> = ArrayList()

    private var isImmutable : Boolean = false
    
    init {
        add( initialEntry )
        entries.forEach { entry ->
            add( entry )
        }
    }
    
    val changeMutability : Boolean
        get() {
            isImmutable = !isImmutable
            return isImmutable
        }
    
    fun entries() : List<T> = list
    
    fun add( entry : T ) {
        if ( isImmutable ) throw SelectableMenuIsImmutableException( this )
        if ( ! list.contains( entry ) )list.add( entry )
    }

    fun remove( entry : T ) {
        if ( isImmutable ) throw SelectableMenuIsImmutableException( this )
        if ( list.contains( entry ) ) list.remove( entry )
    }

    fun select( entry : T ) {
        if ( ! list.contains( entry ) ) throw SelectableMenuEntryNotFoundException( entry.toString() )
        selectedElement.value = entry
    }
    
    override fun toString(): String {
        return """
            |object hash  : ${this.hashCode()}
            |active value : ${selectedElement.value}
            |list         : ${list.toString().let { 
                it.substring( 1 , it.length-1 )
            }}
        """.trimMargin( "|" )
    }
    
}