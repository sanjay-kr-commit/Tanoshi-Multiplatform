package tanoshi.multiplatform.common.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import tanoshi.multiplatform.common.extension.Entry
import tanoshi.multiplatform.common.extension.annotations.TAB
import tanoshi.multiplatform.common.extension.annotations.Variable
import tanoshi.multiplatform.common.extension.annotations.VariableReciever
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.model.BrowseScreenViewModel
import tanoshi.multiplatform.common.screens.component.DesktopOnlyBackHandler
import tanoshi.multiplatform.common.screens.component.ProgressIndicator
import tanoshi.multiplatform.common.util.FunctionTab
import tanoshi.multiplatform.common.util.VariableInstance
import tanoshi.multiplatform.shared.SharedApplicationData
import java.lang.reflect.Field

@Composable
fun BrowseScreen(
    viewModel : BrowseScreenViewModel ,
    sharedData : SharedApplicationData
) = viewModel.run {

    var message by remember { mutableStateOf( "" ) }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            AnimatedVisibility(  !preprosessingData && activeCallbackFunctionHash == searchFunction.hashCode() , enter = slideIn {
                IntOffset( 0 , it.height * -1 )} , exit = slideOut { IntOffset( 0 ,  it.height * -1 ) }
            ) {
                SearchBar( onEnter = {
                    reset()
                    searchField.value = this
                    launchedSearch = true
                } ) {
                    launchedSearch = false
                }
            } },
        bottomBar = {
            Column( modifier = Modifier.fillMaxWidth() ) {
                LazyRow ( modifier = Modifier.fillMaxWidth().wrapContentHeight() ) {
                    tabList.forEach { ( _ , tabFunction , tabName , variableUniqueNameList ) ->
                        item {
                            Box( modifier = Modifier.padding( 5.dp )
                                .clip( RoundedCornerShape( 5.dp ) )
                                .background(
                                    if ( activeCallbackFunctionHash == tabFunction.hashCode() ) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                )
                                .clickable {
                                    variableInUse.clear()
                                    activeCallbackFunction = if ( activeCallbackFunctionHash == tabFunction.hashCode() ) {
                                        variableInUse += searchFunctionVariableList
                                        searchFunction
                                    } else {
                                        variableInUse += variableUniqueNameList
                                        tabFunction
                                    }
                                }
                                .padding( 5.dp ) ,
                                 contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    tabName ,
                                    color = if ( activeCallbackFunctionHash == tabFunction.hashCode() ) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary ,
                                    )
                            }

                        }
                    }
                }
                LazyRow ( modifier = Modifier.fillMaxWidth().wrapContentHeight() ) {
                    booleanVariable.forEach { ( meta , function ) ->
                        item {
                            AnimatedVisibility( variableInUse.contains( meta.uniqueName ) ) {
                                var state by remember { mutableStateOf( function.first.invoke() ) }

                                Box( modifier = Modifier.padding( 5.dp )
                                    .clip( RoundedCornerShape( 5.dp ) )
                                    .background(
                                        if ( state ) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                    )
                                    .clickable {
                                        try {
                                            state = function.second.invoke( !state )
                                        } catch ( _ : Exception ) {}
                                    }
                                    .padding( 5.dp ) ,
                                     contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        meta.publicName ,
                                        color = if ( state ) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary ,
                                        )
                                }
                            }
                        }
                    }
                }

            }
            DesktopOnlyBackHandler( sharedData )
        }

    ) {
        Box( Modifier.fillMaxSize().padding( it ) ) {
            if ( preprosessingData ) Column ( modifier = Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally ){
                ProgressIndicator()
                Text( message )
            } else ResultGrid( viewModel )
        }
    }

    LaunchedEffect( preprosessingData ) {
        if ( preprosessionJob == null ) preprosessionJob = launch {
            searchFunction = {  pageIndex ->
                extension.search( searchField.value , pageIndex )
            }
            try {
                extension::class.java.getMethod( "search" , String::class.java , Int::class.java ).annotations?.filterIsInstance<VariableReciever>()?.firstOrNull()?.variableUniqueNameList?.forEach {
                    searchFunctionVariableList.add( it )
                }
            } catch ( e : Exception ) {
                sharedData.logger log {
                    ERROR
                    title = "Failed To Variable Reciever Name"
                    e.stackTraceToString()
                }
            }
            message = "Extracting Tabs"
            extractVariables( extension , extension::class.java , viewModel )
            tabList += extractTabs( extension )
            activeCallbackFunction = searchFunction
            variableInUse += searchFunctionVariableList
            preprosessingData = false
        }
    }

}

@Composable
private fun ConfigTab(
    viewModel: BrowseScreenViewModel
) = viewModel.run {
    var variableRecieverName by remember { mutableStateOf( "" ) }
    LazyVerticalGrid( GridCells.Adaptive( 20.dp ) ) {

    }
}

@Composable
private fun SearchBar(
    onEnter: String.() -> Unit ,
    onBackspace : () -> Unit
) {
    val searchField = remember { mutableStateOf( "" ) }
    TextField( searchField.value , {
        searchField.value = it
    } , modifier = Modifier
        .fillMaxWidth()
        .padding( 10.dp )
        .clip( RoundedCornerShape( 10.dp ) )
        .onKeyEvent {
            when (it.key.keyCode) {
                Key.Enter.keyCode -> {
                    searchField.value.onEnter()
                    true
                }
                Key.Backspace.keyCode -> {
                    onBackspace()
                    true
                }
                else -> false
            }
        } ,
         colors = TextFieldDefaults.colors(
             disabledTextColor = Color.Transparent,
             focusedIndicatorColor = Color.Transparent,
             unfocusedIndicatorColor = Color.Transparent,
             disabledIndicatorColor = Color.Transparent
         ) ,
        label = {
            Text( "Search" )
        } ,
        singleLine = true
    )
}

@Composable
private fun ResultGrid(
    viewModel: BrowseScreenViewModel
) = viewModel.run {
    var loading : Job? by remember { mutableStateOf( null ) }
    LazyVerticalGrid( GridCells.Adaptive( 60.dp ) ) {
        entries.forEach { entry ->
            item {
                Box( modifier = Modifier.size( 60.dp ) , contentAlignment = Alignment.Center ) {
                    Text( entry.name?:"None" )
                }
            }
        }
        item {
            if ( !isEnded ) {
                if ( activeCallbackFunctionHash != searchFunction.hashCode() || ( activeCallbackFunctionHash == searchFunction.hashCode() && launchedSearch ) ) {
                    Box( modifier = Modifier.size( 60.dp ) , contentAlignment = Alignment.Center ) {
                        ProgressIndicator()
                    }
                    if ( loading == null ) loading = CoroutineScope(Dispatchers.IO).launch {
                        println( "Entered $pageIndex" )
                        fetchList
                        loading = null
                        println( "Exited" )
                    }

                }
            } else {
                Box( modifier = Modifier.size( 60.dp ) , contentAlignment = Alignment.Center ) {
                    Text( "End" )
                }
            }
        }
    }
    DisposableEffect( Unit ) {
        onDispose {
            loading?.cancel()
            loading = null
            println( "Disposed" )
        }
    }
}

@Suppress("UNCHECKED_CAST")
private fun extractTabs(
    extension : Extension<*>
) : List<FunctionTab> = extension::class.java.methods.filter { method ->
    method?.annotations?.filterIsInstance<TAB>()?.isNotEmpty() ?: false
}.filter { method ->
    method?.parameterTypes?.let { argumentList ->
        argumentList.size == 1 && argumentList.first() == Int::class.java
    } ?: false && method?.returnType?.let { returnType ->
        returnType == List::class.java
    } ?: false
}.map { tabFunction ->
   FunctionTab(
       tabFunction , { pageIndex ->
           (tabFunction.invoke( extension , pageIndex ) as List<*>) as List<Entry<*>>
       } ,
       tabFunction.getAnnotation( TAB::class.java ).fieldName ,
       tabFunction.getAnnotation( VariableReciever::class.java )?.variableUniqueNameList?.toList() ?: listOf()
   )
}

private fun extractVariables(
    obj : Any ,
    containerClass : Class<*> ,
    viewModel : BrowseScreenViewModel ,
    depthAllowed : Int = 10
) {
    if ( depthAllowed == 0 ) return

    val variableList = arrayListOf<Field>()
    val nonVariableList = arrayListOf<Field>()

    // split field based on annotation
    containerClass.declaredFields
        .forEach { field ->
            if ( field.annotations.filterIsInstance<Variable>().isEmpty() ) nonVariableList.add( field )
            else variableList.add( field )
        }

    // map variable to lambda
    variableList.forEach { variable ->
            try {
                variable.isAccessible = true
                val annotation = variable.getAnnotation( Variable::class.java )!!
                when ( variable.get( obj ) ) {
                    is Boolean -> {
                        viewModel.booleanVariable += VariableInstance(
                            variable ,
                            annotation.uniqueName ,
                            annotation.publicName
                        ) to (
                             {
                                 variable.get( obj ) as Boolean
                             } to
                             { newValue ->
                                 variable.set( obj,newValue )
                                 variable.get( obj ) as Boolean
                             }
                        )
                    }
                    is String -> {
                        viewModel.stringVariable += VariableInstance(
                            variable ,
                            annotation.uniqueName ,
                            annotation.publicName
                        ) to (
                             {
                                 variable.get( obj ) as String
                             } to
                             { newValue ->
                                 variable.set( obj,newValue )
                                 variable.get( obj ) as String
                             }
                        )
                    }
                    is Int -> {
                        viewModel.intVariable += VariableInstance(
                            variable ,
                            annotation.uniqueName ,
                            annotation.publicName
                        ) to (
                             {
                                 variable.get( obj ) as Int
                             } to
                             { newValue ->
                                 variable.set( obj,newValue )
                                 variable.get( obj ) as Int
                             }
                        )
                    }
                }
            } catch ( _ : Exception ) {}
        }

    // recursive search in child class
    nonVariableList.forEach { field ->
        try {
            field.isAccessible = true
            extractVariables(
                obj = field.get( obj ) ,
                containerClass = field.type ,
                viewModel = viewModel ,
                depthAllowed = depthAllowed-1
            )
        } catch ( _ : Exception ) {}
    }

}