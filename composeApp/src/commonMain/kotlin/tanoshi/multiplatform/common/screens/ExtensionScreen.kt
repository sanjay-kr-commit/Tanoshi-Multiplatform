package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.onClick
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.shared.extension.ExtensionManager

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
                            .onClick {
                                throw Exception( "EXTENSION BROWSE VIEW NOT IMPLEMENTED" )
                            }
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        with( extensionManager ) {
                            extension.icon()
                        }
                        Text( text = extension.name )
                    }
                }
            }
        }




    }
}