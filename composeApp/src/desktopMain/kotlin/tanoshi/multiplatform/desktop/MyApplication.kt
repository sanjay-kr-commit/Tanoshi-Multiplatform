@file: JvmName( "MyApplication" )
package tanoshi.multiplatform.desktop

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import tanoshi.multiplatform.common.screens.LogScreen
import tanoshi.multiplatform.desktop.util.WindowStack
import tanoshi.multiplatform.desktop.util.customApplication
import tanoshi.multiplatform.shared.SharedApplicationData
import java.io.File

fun main() : Unit = SharedApplicationData(

).apply {
//    extensionManager.uninstall( "com.sanjay.extension" )
//    extensionManager.install( "com.sanjay.extension" , File( "/home/sanjay/projects/Tanoshi-Multiplatform/extensions/build/libs/extensions-jvm.jar.tanoshi" ) )
//    extensionManager.loadExtensions()
}.run {

    val windowStack = WindowStack( App() , this )


    customApplication( this ) {
        Window( onCloseRequest = ::exitApplication , state = windowState) {
            windowStack.render()
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
