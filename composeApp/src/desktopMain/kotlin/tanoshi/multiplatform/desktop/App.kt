package tanoshi.multiplatform.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import tanoshi.multiplatform.common.model.MainScreenViewModel
import tanoshi.multiplatform.common.screens.MainScreen
import tanoshi.multiplatform.shared.util.ApplicationActivity

class App : ApplicationActivity() {
    @Composable
    override fun onCreate() {
        val mainScreenViewModel by remember { mutableStateOf( MainScreenViewModel() ) }
        MainScreen(
            applicationData ,
            mainScreenViewModel
        )
    }
}