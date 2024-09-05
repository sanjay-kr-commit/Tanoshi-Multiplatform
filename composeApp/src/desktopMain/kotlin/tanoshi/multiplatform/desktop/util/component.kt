package tanoshi.multiplatform.desktop.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import tanoshi.multiplatform.shared.SharedApplicationData
import java.awt.Toolkit
import java.awt.event.WindowEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ApplicationScope.redirectErrors(
    sharedApplicationData : SharedApplicationData ,
    content : @Composable () -> Unit
) = sharedApplicationData.run {
    CompositionLocalProvider(
        LocalWindowExceptionHandlerFactory provides WindowExceptionHandlerFactory { window ->
            WindowExceptionHandler {
                logger log {
                    ERROR
                    title = "Uncaught Exception"
                    it.stackTraceToString()
                }
                error = it
                window.dispatchEvent(WindowEvent(window, WindowEvent.WINDOW_CLOSING))
            }
        } ,
        content = content
    ) 
}

fun customApplication(
    sharedApplicationData : SharedApplicationData,
    content: @Composable ApplicationScope.() -> Unit
) = application(
    exitProcessOnExit = false
) {

    val toastWindowState = WindowState()
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenHeight = screenSize.height
    val screenWidth = screenSize.width
    toastWindowState.placement = WindowPlacement.Floating
    toastWindowState.position = WindowPosition( Alignment.BottomCenter )
    toastWindowState.size = DpSize( screenWidth.dp , 30.dp )

    sharedApplicationData.applicationScope = this

    redirectErrors( sharedApplicationData ) {
        content()
        // show toast
        Window( onCloseRequest = ::exitApplication ,
            title = "Tanoshi Toast",
            state = toastWindowState,
            resizable = false ,
            alwaysOnTop = true ,
            undecorated = true ,
            transparent = true ,
            visible = sharedApplicationData.isToastWindowVisible
        ) {
            AnimatedVisibility( sharedApplicationData.isToastWindowVisible , enter = fadeIn() , exit = fadeOut() ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding( horizontal = 5.dp ),
                    contentAlignment = Alignment.Center
                ) {
                    Box( modifier = Modifier.wrapContentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding( 5.dp )
                    ) {
                        Text(sharedApplicationData.toastMessage)
                    }
                }
            }
        }
    }
}
