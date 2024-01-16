@file: JvmName( "MyApplication" )
package tanoshi.multiplatform.desktop

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import tanoshi.multiplatform.common.screens.LogScreen
import tanoshi.multiplatform.shared.SharedApplicationData


@OptIn(ExperimentalComposeUiApi::class)
fun main() : Unit = SharedApplicationData().run {

    logger log {
        DEBUG
       "App Started At $appStartUpTime"
    }

    // app
    app( this )

    // open window if any exception of error was found
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
