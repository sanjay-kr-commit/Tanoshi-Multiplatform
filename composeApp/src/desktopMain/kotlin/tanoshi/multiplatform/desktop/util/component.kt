package tanoshi.multiplatform.desktop.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.*
import tanoshi.multiplatform.shared.SharedApplicationData
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
                    "Uncaught Exception\n${it.stackTraceToString()}"
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
    sharedApplicationData.applicationScope = this
    redirectErrors( sharedApplicationData ) {
        content()
    }
}
