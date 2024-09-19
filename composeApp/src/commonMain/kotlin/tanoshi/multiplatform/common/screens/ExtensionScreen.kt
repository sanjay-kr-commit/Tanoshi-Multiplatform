package tanoshi.multiplatform.common.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.extension.ExtensionPackage
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.extensionIcon
import tanoshi.multiplatform.common.extension.loadExtensionPermissionSettingPage
import tanoshi.multiplatform.common.util.child
import tanoshi.multiplatform.shared.extension.ExtensionManager

@Composable
fun ExtensionScreen(
    extensionManager : ExtensionManager ,
    navigateToBrowseScreen : ( ExtensionPackage , String , Extension<*> ) -> Unit ,
) {
    Scaffold(
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding( 10.dp ),
            //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            extensionManager.extensionLoader.loadedExtensionPackage
                .forEach { extensionPackage ->
                    extensionPackage.loadedExtensionClasses.forEach { (className , extension) ->

                        item {

                            val isSettingPageAvailable = remember { extensionPackage.extensionDir.child( "$className.config" ).isFile }
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
                                            navigateToBrowseScreen( extensionPackage , className , extension )
                                        }
                                        .padding( 2.dp )
                                        .clip( RoundedCornerShape( 10.dp ) )
                                    ,
                                    horizontalArrangement = Arrangement.SpaceBetween ,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Box( modifier = Modifier.size( 60.dp ) , contentAlignment = Alignment.Center ) {
                                        extensionManager.extensionIcon(className)
                                    }
                                    Row( modifier = Modifier.wrapContentSize() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center ) {
                                        Text( text = extension.name )
                                        Spacer( modifier = Modifier.width( 5.dp ) )
                                        if ( isSettingPageAvailable ) Image( Icons.Default.Settings , "" , modifier = Modifier
                                            .clip( CircleShape )
                                            .clickable {
                                                isSettingScreenVisible = !isSettingScreenVisible
                                        } )
                                    }
                                }
                                // extension entry end here

                                // extesion setting page start here
                                if ( isSettingScreenVisible ) Column( modifier = Modifier.fillMaxWidth().padding( 10.dp ).clip( RoundedCornerShape( 10.dp ) ) ) {

                                    extensionManager.extensionLoader.loadExtensionPermissionSettingPage(
                                        className ,
                                        extension ,
                                        extensionPackage.extensionDir.child( "$className.config" ) ,
                                        extensionPackage
                                    )

                                }
                                // extension setting page end here

                            }

                        }

                    }
                }
        }
    }
}