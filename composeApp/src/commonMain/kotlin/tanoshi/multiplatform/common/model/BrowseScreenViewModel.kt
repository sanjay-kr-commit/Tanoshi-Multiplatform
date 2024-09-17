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
import tanoshi.multiplatform.common.util.FunctionTab
import tanoshi.multiplatform.common.util.VariableInstance
import tanoshi.multiplatform.shared.ViewModel

class BrowseScreenViewModel : ViewModel() {

    lateinit var extensionPackage : ExtensionPackage
    lateinit var className : String
    lateinit var extension : Extension<*>
    var preprosessingData by mutableStateOf( true )
    var tabList : MutableList<FunctionTab> = mutableStateListOf()
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

    var searchFunctionVariableList : ArrayList<String> = arrayListOf()

    var booleanVariable : MutableList< Pair<VariableInstance,Pair<()->Boolean,(Boolean)->Boolean>>> = mutableStateListOf()
    var stringVariable : MutableList< Pair<VariableInstance,Pair<()->String,(String)->String>>> = mutableStateListOf()
    var intVariable : MutableList< Pair<VariableInstance,Pair<()->Int,(Int)->Int>>> = mutableStateListOf()

    val variableInUse : MutableList<String> = mutableStateListOf()

}