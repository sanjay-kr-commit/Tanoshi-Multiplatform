package tanoshi.multiplatform.desktop

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import tanoshi.multiplatform.shared.naviagtion.NavigationController
import tanoshi.multiplatform.shared.naviagtion.*

fun application() = application {
    val navController by NavController( "Hello" )
    Window( onCloseRequest = ::exitApplication ) {
        screens( navController )
    }
}

@Composable
fun screens( navController : NavigationController ) : Unit = navController.run {
    CreateScreenCatalog {
        Screen( "Hello" ) {
            Hello()
        }
        Screen( "Settings" ) {
            Settings()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationController.Settings() {
    Text( "Settings" ,
          modifier = Modifier.onClick {
              this navigateTo "Hello"
          }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationController.Hello() {
    Text( "Hello" ,
        modifier = Modifier.onClick {
            this navigateTo "Settings"
        }
    )
}