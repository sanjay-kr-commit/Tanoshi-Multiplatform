package tanoshi.multiplatform.common.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.db.Preferences.preferenceByGroupName
import tanoshi.multiplatform.common.db.Preferences.updatePreference
import tanoshi.multiplatform.common.db.Preferences.withPrefix
import tanoshi.multiplatform.common.extension.ExtensionPackage
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.enum.SharedDependencyFields
import tanoshi.multiplatform.common.extension.extensionIcon
import tanoshi.multiplatform.shared.extension.ExtensionLoader
import tanoshi.multiplatform.shared.extension.ExtensionManager

@Composable
fun ExtensionScreen(
    extensionManager : ExtensionManager ,
    navigateToBrowseScreen : ( ExtensionPackage , String , Extension<*> ) -> Unit ,
) {
    Scaffold {

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding( 10.dp ),
            //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            extensionManager.extensionLoader.loadedExtensionPackage
                .forEach { extensionPackage ->
                    extensionPackage.loadedExtensionClasses.forEach { (className , extension) ->

                        item {

                            val isSettingPageAvailable = remember { className.preferenceByGroupName.isNotEmpty() }
                            var isSettingScreenVisible by remember { mutableStateOf( false ) }

                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .wrapContentHeight()
                                    .animateContentSize()
                            ) {

                                // extension entry start here
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .clip( RoundedCornerShape( 10.dp ) )
                                        .clickable {
                                            navigateToBrowseScreen( extensionPackage , className , extension )
                                        }
                                        .padding( 2.dp )
                                        .clip( RoundedCornerShape( 10.dp ) )
                                    ,
                                    horizontalArrangement = Arrangement.SpaceBetween ,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Box( modifier = Modifier.size( 60.dp ) , contentAlignment = Alignment.Center ) {
                                        extensionManager.extensionIcon(className)
                                    }
                                    Row( modifier = Modifier.wrapContentSize() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center ) {
                                        Text( text = extension.name )
                                        Spacer( modifier = Modifier.width( 5.dp ) )
                                        if ( isSettingPageAvailable ) Image( imageVector = Icons.Default.Settings , "" , modifier = Modifier
                                            .clip( CircleShape )
                                            .clickable {
                                                isSettingScreenVisible = !isSettingScreenVisible
                                        } , colorFilter = ColorFilter.tint(
                                            MaterialTheme.colorScheme.onBackground
                                        ) )
                                    }
                                }
                                // extension entry end here

                                // extension setting page start here
                                if ( isSettingScreenVisible ) Column( modifier = Modifier.fillMaxWidth().padding( 10.dp ).clip( RoundedCornerShape( 10.dp ) ) ) {

                                    extensionManager.extensionLoader.loadExtensionPermissionSettingPage(
                                        className ,
                                        extensionPackage
                                    )

                                }
                                // extension setting page end here

                            }

                        }

                    }
                }
        }
    }
}

@Composable
fun ExtensionLoader.loadExtensionPermissionSettingPage(
    className : String ,
    extensionPackage: ExtensionPackage
) {

    val entries : ArrayList<Pair<SharedDependencyFields,MutableState<Boolean>>> = remember { arrayListOf() }
    var job : Job? by remember { mutableStateOf( null ) }
    var updateJob : Job? = remember { null }
    if ( job != null ) {
        Text( "Loading" )
        LinearProgressIndicator( modifier = Modifier.fillMaxWidth() )
    } else entries.forEach { ( key , value ) ->
        Row( modifier = Modifier.fillMaxWidth().padding( 10.dp ) , horizontalArrangement = Arrangement.SpaceBetween ) {
            Text( key.name )
            Image(
                if ( value.value ) Icons.Filled.Check else Icons.Filled.Close , "" ,
                modifier = Modifier
                    .clip( CircleShape )
                    .clickable {
                        if ( updateJob == null ) updateJob = CoroutineScope( Dispatchers.IO ).launch {
                            key.name updatePreference (!value.value).toString()
                            value.value = !value.value
                            reloadClass( className , extensionPackage )
                            updateJob = null
                        }
                    } ,
                colorFilter = ColorFilter.tint( MaterialTheme.colorScheme.onBackground )
            )
        }
    }

    LaunchedEffect( Unit ) {
        job = launch {
            className.withPrefix.forEach { (key, value) ->
                entries += SharedDependencyFields.valueOf( key ) to mutableStateOf( value.toBoolean() )
            }
            job = null
        }
    }

    DisposableEffect( Unit ) {
        onDispose {
            job?.cancel()
            updateJob?.cancel()
        }
    }
}