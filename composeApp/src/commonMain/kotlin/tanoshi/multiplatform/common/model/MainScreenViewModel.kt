package tanoshi.multiplatform.common.model

import tanoshi.multiplatform.common.naviagtion.NavigationController
import tanoshi.multiplatform.common.naviagtion.navController
import tanoshi.multiplatform.common.screens.MainScreen
import tanoshi.multiplatform.shared.ViewModel

class MainScreenViewModel : ViewModel() {
    
    val navController : NavigationController = navController( startScreen = MainScreen.BrowseScreen.name )
    
}