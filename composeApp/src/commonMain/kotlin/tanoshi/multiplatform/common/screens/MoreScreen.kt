package tanoshi.multiplatform.common.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.model.MoreScreenPersistentData
import tanoshi.multiplatform.common.navigation.CreateScreenCatalog
import tanoshi.multiplatform.common.navigation.NavigationController
import tanoshi.multiplatform.common.navigation.Screen
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.util.BackHandler

@Composable
fun MoreScreen( sharedApplicationData : SharedApplicationData , moreScreenPersistentData: MoreScreenPersistentData ) = moreScreenPersistentData.run {
    Scaffold(
        bottomBar = {
            Row( modifier = Modifier.fillMaxWidth().wrapContentHeight() , horizontalArrangement = Arrangement.End ) {
                BackHandler(navController.currentScreen != "" ) {
                    navController navigateTo ""
                }
                Spacer( modifier = Modifier.width( 10.dp ) )
            }
        }
    ){
       Box( Modifier.fillMaxSize().padding( it ) ) {
           AnimatedVisibility( navController.currentScreen == "" , enter = fadeIn(), exit = fadeOut() ) {
               SelectionScreen( navController )
           }
           AnimatedVisibility( navController.currentScreen != "" , enter = fadeIn(), exit = fadeOut() ) {
               sharedApplicationData.MoreScreenCatalog( navController )
           }
       }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SelectionScreen( navController : NavigationController ) {
    LazyColumn( Modifier.fillMaxSize().padding( 10.dp ) ) {
        MoreScreenEntries.entries.forEach { screen ->
            item {
                Row (
                    Modifier.fillMaxWidth().wrapContentHeight().padding( 10.dp )
                        .clip( RoundedCornerShape( 5.dp ) )
                        .background( Color.LightGray )
                    .clickable {
                    navController navigateTo screen.name
                } , horizontalArrangement = Arrangement.Start ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer( modifier = Modifier.width( 5.dp ) )
                    Icon( screen.icon , screen.label )
                    Spacer( modifier = Modifier.width( 5.dp ) )
                    Text( screen.label , modifier = Modifier.padding( 5.dp ) )
                }
            }
        }
    }
}

private enum class MoreScreenEntries(
    val label : String ,
    val icon : ImageVector
) {
    LogScreen( "Log Screen" , Icons.Filled.Error ),
    InternalStateScreen( "Internal State" , Icons.Filled.Info ) ,
    DeveloperOptions( "Developer Option" , Icons.Filled.DeveloperMode )
}

@Composable
private fun SharedApplicationData.MoreScreenCatalog(
    navController : NavigationController
) {
    CreateScreenCatalog( navController ) {
        Screen( MoreScreenEntries.LogScreen.name ) {
            LogScreen( this@MoreScreenCatalog.logger )
        }
        Screen( MoreScreenEntries.InternalStateScreen.name ) {
            InternalStateScreen( this@MoreScreenCatalog )
        }
        Screen( MoreScreenEntries.DeveloperOptions.name ) {
            DeveloperOptionsScreen( this@MoreScreenCatalog )
        }
    }
}