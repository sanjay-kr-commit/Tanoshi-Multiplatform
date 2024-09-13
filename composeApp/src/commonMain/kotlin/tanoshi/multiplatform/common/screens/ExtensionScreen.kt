package tanoshi.multiplatform.common.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.extension.extensionIcon
import tanoshi.multiplatform.shared.extension.ExtensionManager

@Composable
fun ExtensionScreen(
    extensionManager : ExtensionManager ,
    navigateToBrowseScreen : () -> Unit ,
) {
    Scaffold(
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding( 10.dp ),
            //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            extensionManager.extensionLoader.loadedExtensionClasses.forEach { ( packageNameAndArchive , extension ) ->
                item {

                    var isSettingScreenVisible by remember { mutableStateOf( false ) }

                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .wrapContentHeight()
                            .animateContentSize()
                    ) {

                        // extension entry start here
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clip( RoundedCornerShape( 10.dp ) )
                                .clickable {
                                    navigateToBrowseScreen()
                                }
                                .padding( 2.dp )
                                .clip( RoundedCornerShape( 10.dp ) )
                            ,
                            horizontalArrangement = Arrangement.SpaceBetween ,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Box( modifier = Modifier.size( 60.dp ) , contentAlignment = Alignment.Center ) {
                                extensionManager.extensionIcon(packageNameAndArchive.first)
                            }
                            Row( modifier = Modifier.wrapContentSize() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center ) {
                                Text( text = extension.name )
                                Spacer( modifier = Modifier.width( 5.dp ) )
                                Image( Icons.Default.Settings , "" , modifier = Modifier
                                    .clip( CircleShape )
                                    .clickable {
                                        println( isSettingScreenVisible )
                                        isSettingScreenVisible = !isSettingScreenVisible
                                } )
                            }
                        }
                        // extension entry end here

                        // extesion setting page start here
                        if ( isSettingScreenVisible ) Column( modifier = Modifier.fillMaxWidth().padding( 10.dp ).clip( RoundedCornerShape( 10.dp ) ) ) {
                            TODO()
                        }
                        // extension setting page end here

                    }

                }
            }
        }

    }
}