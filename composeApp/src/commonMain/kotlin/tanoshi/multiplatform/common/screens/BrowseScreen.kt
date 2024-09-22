package tanoshi.multiplatform.common.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.extension.Entry
import tanoshi.multiplatform.common.extension.annotations.ExportComposable
import tanoshi.multiplatform.common.extension.annotations.ExportTab
import tanoshi.multiplatform.common.extension.annotations.Variable
import tanoshi.multiplatform.common.extension.annotations.VariableReciever
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.model.BrowseScreenViewModel
import tanoshi.multiplatform.common.screens.component.ProgressIndicator
import tanoshi.multiplatform.common.util.FunctionTab
import tanoshi.multiplatform.common.util.ImageCaching.loadImage
import tanoshi.multiplatform.common.util.VariableInstance
import tanoshi.multiplatform.common.util.toast.ToastTimeout
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.finish
import tanoshi.multiplatform.shared.util.BackHandler
import tanoshi.multiplatform.shared.util.loadImageBitmap
import tanoshi.multiplatform.shared.util.toast.showToast
import java.io.File
import java.lang.reflect.Field

@Composable
fun BrowseScreen(
    viewModel : BrowseScreenViewModel ,
    sharedData : SharedApplicationData
) = viewModel.run {

    var message by remember { mutableStateOf( "" ) }
    var isComposableButtonVisible by remember { mutableStateOf( false ) }
    var isConfigTabVisible by remember { mutableStateOf( false ) }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            AnimatedVisibility(  !isConfigTabVisible &&!isComposableButtonVisible &&!preprosessingData && activeCallbackFunctionHash == searchFunction.hashCode() , enter = slideIn {
                IntOffset( 0 , it.height * -1 )} , exit = slideOut { IntOffset( 0 ,  it.height * -1 ) }
            ) {
                SearchBar( viewModel.searchField.value , onEnter = {
                    reset()
                    searchField.value = this
                    launchedSearch = true
                } ) {
                    launchedSearch = false
                }
            } },
        bottomBar = {

            if ( !isComposableButtonVisible && !isConfigTabVisible ) LazyRow ( modifier = Modifier.fillMaxWidth().wrapContentHeight() ) {
                exportedTabs.forEach { ( _ , tabFunction , tabName , variableUniqueNameList ) ->
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
            Column ( modifier = Modifier.fillMaxWidth() , horizontalAlignment = Alignment.End ) {
                if ( !isComposableButtonVisible && !isConfigTabVisible ) Row {
                    if ( exportedComposable.isNotEmpty() ) AnimatedVisibility( ! isComposableButtonVisible ) {
                        Box( modifier = Modifier
                            .padding( 5.dp )
                            .wrapContentSize()
                            .clip( RoundedCornerShape( 10.dp ) )
                            .clickable {
                                isComposableButtonVisible = true
                            }
                            .background( MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f) )
                            .padding( 5.dp ) , contentAlignment = Alignment.Center
                        ) {
                            Image( Icons.Filled.Star , "" )
                        }
                    }
                    if ( variableInUse.isNotEmpty() ) AnimatedVisibility( !isConfigTabVisible ) {
                        Box( modifier = Modifier
                            .padding( 5.dp )
                            .wrapContentSize()
                            .clip( RoundedCornerShape( 10.dp ) )
                            .clickable {
                                isConfigTabVisible = true
                            }
                            .background( MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f) )
                            .padding( 5.dp ) , contentAlignment = Alignment.Center
                        ) {
                            Image( Icons.Filled.Settings , "" )
                        }
                    }
                }
                BackHandler( true ) {
                    when {
                        isConfigTabVisible -> isConfigTabVisible = false
                        isComposableButtonVisible -> isComposableButtonVisible = false
                        else -> sharedData.finish
                    }
                }
            }
        }

    ) {
        Box( Modifier.fillMaxSize().padding( top = it.calculateTopPadding() ) ) {
            when {
                preprosessingData -> Column ( modifier = Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally ){
                    ProgressIndicator()
                    Text( message )
                }
                isConfigTabVisible -> ExportedVariable( viewModel )
                isComposableButtonVisible -> ExportedComposableButton(sharedData, viewModel)
                else -> ResultGrid( viewModel , sharedData.coroutineIoScope , sharedData.appCacheDir ) {
                    sharedData.showToast( "TODO" , ToastTimeout.SHORT )
                }
            }
        }
    }

    LaunchedEffect( entries.size ) {
        println( entries.size )
    }

    LaunchedEffect( preprosessingData ) {
        if ( preprosessionJob == null ) preprosessionJob = launch {
            searchFunction = {  pageIndex ->
                extension.search( searchField.value , pageIndex )
            }
            try {
                extension::class.java.getMethod( "search" , String::class.java , Int::class.java ).annotations.filterIsInstance<VariableReciever>().firstOrNull()?.variableUniqueNameList?.forEach {
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
            extractExportedFunctions( extension, exportedTabs, exportedComposable )
            activeCallbackFunction = searchFunction
            variableInUse += searchFunctionVariableList
            preprosessingData = false
        }
    }

}

@Composable
private fun ExportedComposableButton(
    sharedData : SharedApplicationData ,
    viewModel: BrowseScreenViewModel
) = viewModel.run {

    LazyColumn ( modifier = Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally ) {

        item {
            Spacer( Modifier.height( 5.dp ) )
            Text( "Start Dynamic Ui" )
        }

        exportedComposable.forEach { ( exportedFunctionName , exportedComposeFunction ) ->
            item {

                Box( modifier = Modifier
                    .padding( 5.dp )
                    .wrapContentSize()
                    .clip( RoundedCornerShape( 10.dp ) )
                    .clickable {
                        try {
                            exportedComposeFunction()
                        } catch ( e : java.lang.reflect.InvocationTargetException ) {
                            sharedData.showToast( "Failed to launch $exportedFunctionName From ${extension::class.java.packageName}" , ToastTimeout.SHORT )
                            sharedData.logger log {
                                ERROR
                                title = "Failed to launch $exportedFunctionName From ${extension::class.java.packageName}"
                                e.targetException.stackTraceToString()
                            }
                        }
                    }
                    .background( MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f) )
                    .padding( 5.dp ) , contentAlignment = Alignment.Center
                ) {
                    Text( exportedFunctionName , color = Color.White )
                }
            }

        }

    }

}


@Composable
private fun ExportedVariable(
    viewModel: BrowseScreenViewModel
) = viewModel.run {
    LazyColumn ( modifier = Modifier.fillMaxSize() ) {

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

@Composable
private fun SearchBar(
    initialSearchField : String,
    onEnter: String.() -> Unit ,
    onBackspace : () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchField = remember { mutableStateOf( initialSearchField ) }
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
        singleLine = true ,
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                searchField.value.onEnter()
            } ,
            onSend = {
                keyboardController?.hide()
                searchField.value.onEnter()
            }
        )
    )
}

@Composable
private fun ResultGrid(
    viewModel: BrowseScreenViewModel ,
    coroutineIoScope : CoroutineScope ,
    cacheDir : File ,
    navigationTo : Entry<*>.() -> Unit
) = viewModel.run {
    var loading : Job? by remember { mutableStateOf( null ) }
    LazyVerticalStaggeredGrid(
        StaggeredGridCells.Adaptive( 150.dp )
    ) {
        entries.forEach { entry ->
            item {
                var cover : ImageBitmap? by remember { mutableStateOf( null ) }
                var coverLoadJob : Job? = null

                Column ( modifier = Modifier.width( 150.dp ).clickable {
                    entry.navigationTo()
                }, horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center ) {
                    AnimatedVisibility( cover != null ) {
                        Image( cover!! , entry.name?:"None" , contentScale = ContentScale.FillWidth , modifier = Modifier
                            .fillMaxWidth()
                            .padding( 5.dp )
                            .clip(RoundedCornerShape( 10.dp )) )

                    }
                    AnimatedVisibility( cover == null ) {
                        Image( Icons.Filled.BrokenImage , "" , modifier = Modifier
                            .fillMaxWidth()
                            .height( 100.dp )
                            .padding( 5.dp )
                            .clip(RoundedCornerShape( 10.dp ))
                            .background( MaterialTheme.colorScheme.inversePrimary )
                        )
                    }
                    Text( entry.name?: "Null" )
                }

                LaunchedEffect( Unit ) {
                    if ( coverLoadJob == null ) coverLoadJob = coroutineIoScope.launch {
                        try {
                            entry.coverArt?.let {
                                cover = loadImageBitmap( loadImage(
                                    extension.domainsList.activeElementValue + it, cacheDir
                                ))
                            }
                        } catch ( e : java.lang.Exception ) {
                            println( e.stackTraceToString() )
                        }
                    }
                }

            }
        }
        item {
            if ( !isEnded && (activeCallbackFunctionHash != searchFunction.hashCode() || ( activeCallbackFunctionHash == searchFunction.hashCode() && launchedSearch )) ) {
                Box( modifier = Modifier.fillMaxWidth().height( 150.dp ) , contentAlignment = Alignment.Center ) {
                    ProgressIndicator()
                }
                if ( loading == null ) loading = CoroutineScope(Dispatchers.IO).launch {
                    fetchList
                    loading = null
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
private fun extractExportedFunctions(
    extension : Extension<*> ,
    exportedTabs : MutableList<FunctionTab> ,
    exportedComposable : MutableList<Pair<String,()->Unit>>
){

    extension::class.java.methods.forEach { method ->
        val annotationList = method?.annotations ?: arrayOf()
        when {
            // Exported Tab
            annotationList.filterIsInstance<ExportTab>().isNotEmpty() &&
            method?.parameterTypes?.let { argumentList ->
                argumentList.size == 1 && argumentList.first() == Int::class.java
            } ?: false && method?.returnType?.let { returnType ->
                returnType == List::class.java
            } ?: false -> {
                //
                method.getAnnotation( ExportTab::class.java )?.let {
                    exportedTabs += FunctionTab(
                        method , { pageIndex ->
                            (method.invoke( extension , pageIndex ) as List<*>) as List<Entry<*>>
                        } ,
                        it.tabName ,
                        method.getAnnotation( VariableReciever::class.java )?.variableUniqueNameList?.toList() ?: listOf()
                    )
                }
            }

            // Exported Composable
            annotationList.filterIsInstance<ExportComposable>().isNotEmpty() &&
            method?.parameterCount == 0 -> {
                method.getAnnotation( ExportComposable::class.java )?.let {
                    exportedComposable += it.composableFunctionName to {
                        method.invoke( extension )
                    }
                }
            }

        }

    }

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
                obj = field.get( obj ) ?: { throw Exception( "Recieved Null" ) },
                containerClass = field.type ,
                viewModel = viewModel ,
                depthAllowed = depthAllowed-1
            )
        } catch ( _ : Exception ) {}
    }

}