package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.util.Platform
import tanoshi.multiplatform.shared.util.PLATFORM
import tanoshi.multiplatform.shared.util.BackHandler
import java.io.File
import java.util.*

@Composable
fun FilePicker(
    root : File ,
    exit : () -> Unit ,
    showHiddenFile : Boolean = false ,
    onPick : ((File) -> Unit)? = null
) {

    val stack = remember { Stack<File>()}
    var currentDir by remember { mutableStateOf( root ) }
    var dirContent by remember { mutableStateOf( currentDir.listFiles()?.let { it ->
        if ( showHiddenFile ) it.toList()
        else it.filter { !it.name.startsWith( "." ) }
    } ) }

    Scaffold(
        bottomBar = {
            Box(
                Modifier.fillMaxWidth() , contentAlignment = Alignment.BottomEnd
            ) {
                onPick?.let {
                    Button( onClick = {
                        onPick( currentDir )
                    } , modifier = Modifier.padding( 5.dp ).fillMaxWidth() ) {
                        Text( "Pick This Folder" )
                    }
                }
                BackHandler( true ) {
                    if ( stack.isNotEmpty() ) {
                        currentDir = stack.pop()
                        dirContent = currentDir.listFiles()?.let { it ->
                            if ( showHiddenFile ) it.toList()
                            else it.filter { !it.name.startsWith( "." ) }
                        }
                    } else exit()
                }
            }
        }
    ) {

        LazyColumn( modifier = Modifier.padding( it ) ) {

            dirContent?.forEach { entry ->

                item {
                    Box( modifier = Modifier
                        .fillMaxWidth()
                        .padding( 5.dp )
                        .clip( RoundedCornerShape( 10.dp ) )
                        .clickable {
                            if ( entry.isDirectory ) {
                                stack.push( currentDir )
                                currentDir = entry
                                dirContent = currentDir.listFiles()?.let { it ->
                                    if ( showHiddenFile ) it.toList()
                                    else it.filter { !it.name.startsWith( "." ) }
                                }
                            } else onPick?.let {
                                onPick( entry )
                            } ?: run {
                                if ( Platform.Desktop == PLATFORM ) ProcessBuilder().apply {
                                    command( "xdg-open" , entry.absolutePath )
                                    start()
                                }
                            }
                        }
                        .background(
                            if ( entry.isDirectory ) MaterialTheme.colorScheme.secondaryContainer
                            else MaterialTheme.colorScheme.primaryContainer
                        )
                        .padding( 5.dp )
                    ) {
                        Text( entry.name )
                    }
                }

            }

        }

    }

}