@file: JvmName( "MyApplication" )
package tanoshi.multiplatform.desktop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.screens.LogScreen
import tanoshi.multiplatform.desktop.util.WindowStack
import tanoshi.multiplatform.desktop.util.customApplication
import tanoshi.multiplatform.shared.SharedApplicationData

fun main() : Unit = SharedApplicationData(

).run {

    val windowStack = WindowStack( App() , this )

    customApplication( this ) {

        Window( onCloseRequest = ::exitApplication , state = windowState) {
            windowStack.render()
        }

        // show toast
        Window( onCloseRequest = ::exitApplication ,
            resizable = false ,
            alwaysOnTop = true ,
            undecorated = true ,
            transparent = true ,
            visible = isToastWindowVisible
        ) {
            AnimatedVisibility( isToastWindowVisible , enter = fadeIn() , exit = fadeOut() ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(toastMessage)
                }
                LaunchedEffect(null) {
                    if (toastJob == null) toastJob = CoroutineScope(Dispatchers.IO).launch {
                        while (toastDeque.isNotEmpty()) {
                            toastDeque.poll().let { toast ->
                                toastMessage = toast.first
                                delay(toast.second)
                            }
                        }
                        isToastWindowVisible = false
                        toastJob = null
                    }
                }
            }
        }
    }

    error?.let {
        application( false ) {
            Window( onCloseRequest = ::exitApplication , title = "App Log" ,
                    icon = rememberVectorPainter(
                        Icons.Filled.Settings
                    )
            ) {
                LogScreen( logger )
            }
        }
    }

}
