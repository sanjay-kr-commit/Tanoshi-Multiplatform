package tanoshi.multiplatform.common.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.extension.Entry
import tanoshi.multiplatform.common.extension.annotations.TAB
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.model.BrowseScreenViewModel
import tanoshi.multiplatform.common.screens.component.DesktopOnlyBackHandler
import tanoshi.multiplatform.common.screens.component.ProgressIndicator
import tanoshi.multiplatform.common.util.toast.ToastTimeout
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.util.toast.showToast

@Composable
fun BrowseScreen(
    viewModel : BrowseScreenViewModel ,
    sharedData : SharedApplicationData
) = viewModel.run {

    var message by remember { mutableStateOf( "" ) }

    Scaffold(
        topBar = {
            AnimatedVisibility(  !preprosessingData && activeCallbackFunctionHash == searchFunction.hashCode() , enter = slideIn {
                IntOffset( 0 , it.height * -1 )
            } , exit = slideOut { IntOffset( 0 ,  it.height * -1 ) } ) {
                SearchBar( searchField ) {
                    sharedData.showToast(
                        searchField.value , ToastTimeout.SHORT
                    )
                }
            }
        },
        bottomBar = {
            Column( modifier = Modifier.fillMaxWidth() ) {
                DesktopOnlyBackHandler( sharedData )
                LazyRow ( modifier = Modifier.fillMaxWidth().wrapContentHeight() ) {
                    tabList.forEach { (tabName, tabFunction) ->
                        item {
                            Box( modifier = Modifier.padding( 5.dp )
                                .clip( RoundedCornerShape( 5.dp ) )
                                .background(
                                    if ( activeCallbackFunctionHash == tabFunction.hashCode() ) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
                                )
                                .clickable {
                                    activeCallbackFunction = if ( activeCallbackFunctionHash == tabFunction.hashCode() )   searchFunction
                                    else tabFunction
                                }
                                .padding( 5.dp ) ,
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    tabName ,
                                      color = if ( activeCallbackFunctionHash == tabFunction.hashCode() ) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSecondary ,
                                )
                            }

                        }
                    }
                }

            }
        }
    ) {
        Box( Modifier.fillMaxSize().padding( it ) ) {
            if ( preprosessingData ) Column ( modifier = Modifier.fillMaxSize() , verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally ){
                ProgressIndicator()
                Text( message )
            } else ResultGrid()
        }
    }

    LaunchedEffect( preprosessingData ) {
        if ( preprosessionJob == null ) preprosessionJob = launch {
            searchFunction = {  pageIndex ->
                extension.search( searchField.value , pageIndex )
            }
            message = "Extracting Tabs"
            tabList += extractTabs( extension )
            activeCallbackFunction = searchFunction
            preprosessingData = false
        }
    }

}

@Composable
private fun SearchBar(
    searchField: MutableState<String>,
    onClick: () -> Unit
) {
    TextField( searchField.value , {
        searchField.value = it
    } , modifier = Modifier
        .fillMaxWidth()
        .padding( 10.dp )
        .clip( RoundedCornerShape( 10.dp ) )
        .onKeyEvent {
            when (it.key.keyCode) {
                Key.Enter.keyCode -> {
                    onClick()
                    true
                }
                else -> false
            }
        } ,
         colors = TextFieldDefaults.textFieldColors(
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
private fun ResultGrid() {
}

@Suppress("UNCHECKED_CAST")
private fun extractTabs(
    extension : Extension<*>
) : List<Pair<String,(Int)->List<Entry<*>>>> = extension::class.java.methods?.filter { method ->
    method?.annotations?.filterIsInstance<TAB>()?.isNotEmpty() ?: false
}?.filter { method ->
    method?.parameterTypes?.let { argumentList ->
        argumentList.size == 1 && argumentList.first() == Int::class.java
    } ?: false && method?.returnType?.let { returnType ->
        returnType == List::class.java
    } ?: false
}?.map { tabFunction ->
    tabFunction.getAnnotation( TAB::class.java ).fieldName to { pageIndex ->
        (tabFunction.invoke( extension , pageIndex ) as List<*>) as List<Entry<*>>
    }
} ?: listOf()