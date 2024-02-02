import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.system.exitProcess

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Hello(
    activeWindow : MutableState< @Composable () -> Unit > = mutableStateOf( {} )
) {
    Window( onCloseRequest = { exitProcess(-1) } ) {
        Text( "Hello" ,
            modifier = Modifier.onClick {
                println( "Hello" )
                activeWindow.value = {
                    Hi( activeWindow )
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Hi(
    activeWindow : MutableState< @Composable () -> Unit > = mutableStateOf( {} )
) {
    Window( onCloseRequest = { exitProcess(-1) } ) {
        Text( "Hi",
            modifier = Modifier.onClick {
                println( "Hi" )
                activeWindow.value = {
                    Hello( activeWindow )
                }
            }
        )
    }
}

val default : @Composable () -> Unit = {}

fun temp() = application {
    val activeWindow : MutableState< @Composable () -> Unit> = remember { mutableStateOf( default ) }
    if ( activeWindow.value == default ) {
        Window( onCloseRequest = ::exitApplication , visible = false ) {

        }
    }
    activeWindow.value()
    LaunchedEffect( null ) {
        activeWindow.value = {
            Hello( activeWindow )
        }
    }
}