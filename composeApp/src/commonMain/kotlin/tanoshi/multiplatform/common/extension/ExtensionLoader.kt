package tanoshi.multiplatform.common.extension

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.core.SharedDependencies
import tanoshi.multiplatform.common.extension.enum.SharedDependencyFields
import tanoshi.multiplatform.shared.extension.ExtensionLoader
import java.io.File

fun ExtensionLoader.loadExtensionPermission(
    className : String ,
    extension: Extension<*> , file : File
) {
    if ( extension !is SharedDependencies ) return
    if ( !file.isFile ) {
        createExtensionPermissionFile( extension , file )
        return
    }
    val buffer = StringBuffer()
    file.bufferedReader().use { bufferedReader ->
        // read shared dependency permission
        repeat( SharedDependencyFields.entries.size ) {
            val pair = bufferedReader.readLine().split( ":" )
            pair.firstOrNull()?.let { buffer.append( it ) }
            if ( pair.size > 1 && pair[1] == "${true}" ) {
                buffer.append( " : True\n" )
                when ( SharedDependencyFields.valueOf( pair.first() ) ) {
                    SharedDependencyFields.StartComposableView -> extension.startComposableView = {
                        startDynamicActivity?.let { (this as (@Composable () -> Unit ) ).it() }
                    }
                    SharedDependencyFields.Logger -> extension.logger = logger
                    SharedDependencyFields.ShowToast -> extension.showToast = setToastLambda
                }
            } else buffer.append( " : False\n" )
        }
    }
    logger?. log {
        DEBUG
        title = "Permission : $className"
        buffer.toString()
    }
}

@Composable
fun ExtensionLoader.loadExtensionPermissionSettingPage(
    className : String ,
    extension: Extension<*> ,
    file : File ,
    extensionPackage: ExtensionPackage
) {
    val entries : ArrayList<Pair<Pair<MutableState<String>,SharedDependencyFields>,MutableState<Boolean>>> = remember { arrayListOf() }
    var job : Job? by remember { mutableStateOf( null ) }
    var updateJob : Job? = remember { null }
    if ( job != null ) {
        Text( "Loading" )
        LinearProgressIndicator( modifier = Modifier.fillMaxWidth() )
    } else entries.forEach {
        Row( modifier = Modifier.fillMaxWidth().padding( 10.dp ) , horizontalArrangement = Arrangement.SpaceBetween ) {
            Text( it.first.second.name )
            Image(
                if ( it.second.value ) Icons.Filled.Check else Icons.Filled.Close , "" ,
                modifier = Modifier
                    .clip( CircleShape )
                    .clickable {
                        if ( updateJob == null ) updateJob = CoroutineScope( Dispatchers.IO ).launch {
                            file.writeText(
                                file.readText()
                                    .replace(
                                        it.first.first.value , "${it.first.second}:${!it.second.value}".also { newLine ->
                                            it.first.first.value = newLine
                                        }
                                    )
                            )
                            it.second.value = !it.second.value
                            reloadClass( className , extensionPackage )
                            updateJob = null
                        }
                    }
            )
        }
    }

    LaunchedEffect( Unit ) {
        job = launch {
            file.bufferedReader().use { bufferedReader ->
                // read shared dependency permission
                repeat( SharedDependencyFields.entries.size ) {
                    val line = bufferedReader.readLine()
                    val pair = line.split( ":" )
                    entries += mutableStateOf( line ) to SharedDependencyFields.valueOf( pair.first() ) to mutableStateOf( pair[1].toBoolean() )
                }
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