package tanoshi.multiplatform.common.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job
import tanoshi.multiplatform.common.exception.EndOfListException
import tanoshi.multiplatform.common.extension.Entry
import tanoshi.multiplatform.common.extension.ExtensionPackage
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.shared.ViewModel
import java.lang.reflect.Method

class BrowseScreenViewModel : ViewModel() {

    lateinit var extensionPackage : ExtensionPackage
    lateinit var className : String
    lateinit var extension : Extension<*>
    var preprosessingData by mutableStateOf( true )
    var tabList : MutableList<Pair<String,(Int)->List<Entry<*>>>> = mutableStateListOf()
    var searchField = mutableStateOf( "" )


    var isEnded by mutableStateOf( false )
    val result : MutableList<Entry<*>> = mutableStateListOf()
    var pageIndex = 1

    val reset : () -> Unit = {
        result.clear()
        pageIndex = 0
    }

    var preprosessionJob : Job? = null

    var activeCallbackFunction : (Int) -> List<Entry<*>> = {
        throw Exception( "Not Call Back Function Attached" )
    }
    set(value) {
        reset()
        activeCallbackFunctionHash = value.hashCode()
        field = value
    }

    var activeCallbackFunctionHash : Int by mutableStateOf( 0 )

    val fetchList : Unit
        get() = try {
            result += activeCallbackFunction( pageIndex++ )
        } catch ( e : EndOfListException ) {
            isEnded = true
        } catch ( e : Exception ) {
            println( e.stackTraceToString() )
        }

    lateinit var searchFunction : (Int) -> List<Entry<*>>

}