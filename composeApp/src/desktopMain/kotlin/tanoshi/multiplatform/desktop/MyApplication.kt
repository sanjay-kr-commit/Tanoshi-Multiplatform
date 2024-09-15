@file: JvmName( "MyApplication" )
package tanoshi.multiplatform.desktop

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import tanoshi.multiplatform.common.screens.LogScreen
import tanoshi.multiplatform.desktop.util.WindowStack.Companion.startWindowStack
import tanoshi.multiplatform.desktop.util.customApplication
import tanoshi.multiplatform.shared.SharedApplicationData

fun main() : Unit = SharedApplicationData().run {

    customApplication( this ) {
        Window( onCloseRequest = ::exitApplication , state = windowState) {
            startWindowStack( InitializeResources() , ::exitApplication )
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
