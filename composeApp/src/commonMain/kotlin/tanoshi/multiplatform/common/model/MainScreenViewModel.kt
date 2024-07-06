package tanoshi.multiplatform.common.model

import tanoshi.multiplatform.common.navigation.NavigationController
import tanoshi.multiplatform.common.navigation.navController
import tanoshi.multiplatform.common.screens.MainScreen
import tanoshi.multiplatform.shared.ViewModel

class MainScreenViewModel : ViewModel() {
    
    val navController : NavigationController = navController( startScreen = MainScreen.HomeScreen.name )
    val moreScreenPersistentData = MoreScreenPersistentData()
    
}