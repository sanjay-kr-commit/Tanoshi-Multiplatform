package tanoshi.multiplatform.desktop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import tanoshi.multiplatform.common.model.MainScreenViewModel
import tanoshi.multiplatform.common.screens.MainScreen
import tanoshi.multiplatform.shared.util.ApplicationActivity

class MainActivity : ApplicationActivity() {

    override fun onCreate() {
        val mainScreenViewModel by mutableStateOf(MainScreenViewModel())
        setContent {
            TanoshiTheme {
                MainScreen(
                    applicationData,
                    mainScreenViewModel
                )
            }
        }
    }
}