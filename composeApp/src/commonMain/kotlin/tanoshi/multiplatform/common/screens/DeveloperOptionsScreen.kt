package tanoshi.multiplatform.common.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tanoshi.multiplatform.common.util.toFile
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.util.PLATFORM
import tanoshi.multiplatform.common.util.Platform
import tanoshi.multiplatform.common.util.toast.ToastTimeout
import tanoshi.multiplatform.common.extension.packageList
import tanoshi.multiplatform.common.extension.uninstall
import tanoshi.multiplatform.shared.util.toast.showToast
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Composable
fun DeveloperOptionsScreen(
    sharedAppData : SharedApplicationData
) {
    val extensionId = remember { mutableStateOf("") }
    val installExtensionPath = remember { mutableStateOf("") }
    var isPopUpVisible by remember { mutableStateOf(false) }
    var uninstallExtension by remember { mutableStateOf(false ) }
    var isFilePickerVisible by remember { mutableStateOf( false ) }

    Column( Modifier.fillMaxSize() ) {
        Button( {
            isPopUpVisible = true
        } ) {
            Text(  "Install Extension File" )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button( {
            uninstallExtension = true
        } ) {
            Text(  "Uninstall Extension File" )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button( {
            isFilePickerVisible = true
        } ) {
            Text(  "Show App Files" )
        }
    }

    AnimatedVisibility( uninstallExtension , modifier = Modifier.fillMaxSize() , enter = fadeIn() , exit = fadeOut() ) {

        Box( modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center ) {
            UninstallExtension( extensionId , {
                uninstallExtension = false
            } , "Uninstall Extension" , sharedAppData.extensionManager.packageList )
        }

        DisposableEffect( uninstallExtension ) {
            onDispose {
                try {
                    if ( !uninstallExtension ) {
                        sharedAppData.extensionManager.uninstall(
                            extensionId.value
                        )
                    }
                } catch ( e : Exception ) {
                    sharedAppData.logger log  {
                        title = "Failed To Uninstall Extension File"
                        e.stackTraceToString()
                    }
                }


            }
        }
    }

    AnimatedVisibility( isPopUpVisible , modifier = Modifier.fillMaxSize() , enter = fadeIn() , exit = fadeOut() ) {

        Box( modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center ) {

            ImportPath( installExtensionPath , {
                isPopUpVisible = false
            } , "Install Extension File" )
        }

        DisposableEffect( isPopUpVisible ) {
            onDispose {

                try {
                    val extensionPath : File? = when {
                        PLATFORM == Platform.Desktop -> installExtensionPath.value.toFile
                        PLATFORM == Platform.Android && installExtensionPath.value
                            .startsWith( sharedAppData.publicDir.absolutePath ) -> File( sharedAppData.publicDir , installExtensionPath.value.replace( sharedAppData.publicDir.absolutePath , "" )  )
                        PLATFORM == Platform.Android && installExtensionPath.value
                            .startsWith( sharedAppData.privateDir.absolutePath ) -> File( sharedAppData.privateDir , installExtensionPath.value.replace( sharedAppData.privateDir.absolutePath , "" )  )
                        PLATFORM == Platform.Android -> File( sharedAppData.publicDir , installExtensionPath.value )
                        else -> null
                    }
                    if ( !isPopUpVisible ) {
                        if (extensionPath != null && extensionPath.isFile) CoroutineScope(
                            Dispatchers.IO
                        ).launch {
                            sharedAppData.extensionManager
                                .install(
                                    extensionPath
                                )
                        } else sharedAppData.showToast("Invalid Path", ToastTimeout.SHORT)
                    }
                } catch ( e : Exception ) {
                    sharedAppData.logger log  {
                        title = "Failed To Install Extension File"
                        e.stackTraceToString()
                    }
                }
            }
        }
    }

    AnimatedVisibility( isFilePickerVisible ) {
        FilePicker( sharedAppData.privateDir , { isFilePickerVisible = false } , true )
    }

}

@Composable
private fun UninstallExtension(
    id : MutableState<String> ,
    exitScreen : () -> Unit ,
    exitButtonMessage : String ,
    listOfPackages : List<String>
){
    LazyColumn (
        modifier = Modifier
            .padding( 10.dp ) ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        listOfPackages.forEach {
            item {
                Button({
                    id.value = it
                    exitScreen()
                }) {
                    Text( it )
                }
            }
        }
    }
}

@Composable
private fun ImportPath(
    path : MutableState<String> ,
    exitScreen : () -> Unit ,
    exitButtonMessage : String
) {
    Column (
        modifier = Modifier
            .padding( 10.dp ) ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField( path.value , onValueChange = {
            path.value = it
        } )

        Spacer(modifier = Modifier.height(10.dp))

        Button( {
            exitScreen()
        } ) {
            Text( exitButtonMessage )
        }

    }
}