package tanoshi.multiplatform.desktop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import tanoshi.multiplatform.common.model.MainScreenViewModel
import tanoshi.multiplatform.common.screens.MainScreen
import tanoshi.multiplatform.desktop.util.customApplication
import tanoshi.multiplatform.shared.SharedApplicationData


@ExperimentalComposeUiApi
fun app(
    sharedApplicationData : SharedApplicationData
) = customApplication( sharedApplicationData ) {

    val mainScreenViewModel by remember { mutableStateOf( MainScreenViewModel() ) }

    Window(
        onCloseRequest = ::exitApplication ,
        state = sharedApplicationData.windowState ,
        title = "Project T"
    ) {

        MainScreen(
            sharedApplicationData ,
            mainScreenViewModel
        )

    }
}