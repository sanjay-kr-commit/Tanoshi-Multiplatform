package tanoshi.multiplatform.common.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.model.MainScreenViewModel
import tanoshi.multiplatform.common.navigation.CreateScreenCatalog
import tanoshi.multiplatform.common.navigation.Screen
import tanoshi.multiplatform.common.util.ApplicationActivityName
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.changeActivity

@Composable
fun MainScreen(
    sharedAppData : SharedApplicationData ,
    viewModel : MainScreenViewModel
) = sharedAppData.run {
    Scaffold {
        if ( portrait ) PortraitMainScreen( viewModel )
        else LandscapeMainScreen( viewModel )
    }
}

@Composable
private fun SharedApplicationData.PortraitMainScreen(
    viewModel : MainScreenViewModel
) {
    Scaffold(
       bottomBar = {
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .wrapContentHeight()
                   .padding( 5.dp ) ,
               horizontalArrangement = Arrangement.SpaceEvenly ,
               verticalAlignment = Alignment.CenterVertically
           ) {
               MainScreen.entries.forEach { screen ->
                   Row(
                       Modifier.clickable {
                           viewModel.navController navigateTo screen.name
                       } ,
                       verticalAlignment = Alignment.CenterVertically
                   ) {  
                       Icon(
                           screen.icon ,
                           screen.label 
                       )
                       AnimatedVisibility(
                           viewModel.navController.currentScreen == screen.name
                       ) {
                           Text(
                               " ${screen.label}" ,
                               modifier = Modifier
                                   .align(Alignment.CenterVertically)
                           )
                       }
                   }
               }
           }
       } 
    ) {
        Box( Modifier.fillMaxSize().padding( it ) , contentAlignment = Alignment.Center ) {
            MainScreenCatalog( viewModel = viewModel )
        }
    }
}

@Composable
private fun SharedApplicationData.LandscapeMainScreen( viewModel: MainScreenViewModel ) {
    Row (
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .padding( 5.dp ) ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            MainScreen.entries.forEach { screen ->
                 Column(
                     modifier = Modifier
                         .clickable {
                             viewModel.navController navigateTo screen.name
                         }
                 ) {
                     Icon(
                         screen.icon ,
                         screen.label
                     )
//                     AnimatedVisibility(
//                         viewModel.navController.currentScreen == screen.label
//                     ) {
//                         Text( screen.label ,
//                               modifier = Modifier
//                                   .align(Alignment.CenterHorizontally)
//                         )
//                     }
                 }
            }

        }


        MainScreenCatalog( viewModel )

    }
}

enum class MainScreen(
    val label : String ,
    val icon : ImageVector
) {
    HomeScreen(
        "Home" ,
        Icons.Filled.Home
    ) ,
    ExtensionScreen(
        "Extensions" ,
        Icons.Filled.Extension
    ),
    HistoryScreen(
        "History" ,
        Icons.Filled.History
    ) ,
    MoreScreen(
        "More" ,
        Icons.Filled.Settings
    )
}

@Composable
private fun SharedApplicationData.MainScreenCatalog(
    viewModel : MainScreenViewModel
) {
    CreateScreenCatalog( viewModel.navController ) {
        Screen( MainScreen.HomeScreen.name ) {
            HomeScreen( this@MainScreenCatalog , viewModel )
        }
        Screen( MainScreen.ExtensionScreen.name ) {
            ExtensionScreen( extensionManager , { extensionPackage , className , extension ->
                changeActivity( ApplicationActivityName.Browse ) {
                    put( "extensionPackage" , extensionPackage )
                    put( "className" , className )
                    put( "extension" , extension )
                }
            } )
        }
        Screen( MainScreen.HistoryScreen.name ) {
            HistoryScreen()
        }
        Screen( MainScreen.MoreScreen.name ) {
            MoreScreen( this@MainScreenCatalog , viewModel.moreScreenPersistentData )
        }
    }
}