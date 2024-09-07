package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.shared.extension.ExtensionManager
import tanoshi.multiplatform.common.extension.extensionIcon

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExtensionScreen(
    extensionManager : ExtensionManager
) {
    Scaffold(
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding( 10.dp ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            extensionManager.extensionLoader.loadedExtensionClasses.forEach { ( packageNameAndArchive , extension ) ->
                item {
                    Row (
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            .clickable {
                                throw Exception( "EXTENSION BROWSE VIEW NOT IMPLEMENTED" )
                            }
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Box( modifier = Modifier.size( 20.dp ) , contentAlignment = Alignment.Center ) {
                            extensionManager.extensionIcon(packageNameAndArchive.first)
                        }
                        Text( text = extension.name )
                    }
                }
            }
        }

    }
}