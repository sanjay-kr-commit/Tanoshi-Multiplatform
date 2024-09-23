package tanoshi.multiplatform.common.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.model.MainScreenViewModel
import tanoshi.multiplatform.common.screens.component.ProgressIndicator
import tanoshi.multiplatform.common.util.ImageCaching.loadImage
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.util.loadImageBitmap

@Composable
fun HomeScreen(
    sharedAppData : SharedApplicationData ,
    viewModel: MainScreenViewModel
) = viewModel.homeScreenPersistantData.run {

    var isDataInitialized by remember { mutableStateOf( false ) }

    if ( !isDataInitialized ) Box( Modifier.fillMaxSize() ) {
        ProgressIndicator()
    } else LazyVerticalStaggeredGrid(
        StaggeredGridCells.Adaptive( 150.dp ) ,
        modifier = Modifier.fillMaxSize()
    ) {
        entries.forEach { (extension,entry) ->
            item {
                var cover : ImageBitmap? by remember { mutableStateOf( null ) }
                var coverLoadJob : Job? = null
                Column ( modifier = Modifier.width( 150.dp )
                 , horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center ) {
                    AnimatedVisibility( cover != null ) {
                        Image( cover!! , entry.name?:"None" , contentScale = ContentScale.FillWidth , modifier = Modifier
                            .fillMaxWidth()
                            .padding( 5.dp )
                            .clip(RoundedCornerShape( 10.dp )) )

                    }
                    AnimatedVisibility( cover == null ) {
                        Image( Icons.Filled.BrokenImage , "" , modifier = Modifier
                            .fillMaxWidth()
                            .height( 100.dp )
                            .padding( 5.dp )
                            .clip(RoundedCornerShape( 10.dp ))
                            .background( MaterialTheme.colorScheme.inversePrimary )
                        )
                    }
                    Text( entry.name?: "Null" )
                }

                LaunchedEffect( Unit ) {
                    val coroutineIoScope = null
                    if ( coverLoadJob == null ) coverLoadJob = sharedAppData.coroutineIoScope.launch {
                        try {
                            entry.coverArt?.let {
                                cover = loadImageBitmap( loadImage(
                                    extension.domainsList.activeElementValue + it, sharedAppData.privateDir
                                ))
                            }
                        } catch ( e : java.lang.Exception ) {
                            println( e.stackTraceToString() )
                        }
                    }
                }

            }
        }
    }


    LaunchedEffect( Unit ) {
        if ( !isDataInitialized ) {
            entries = mutableStateListOf()
            sharedAppData.library.selectAll { extension , extensionPackage ->
                sharedAppData.extensionManager.extensionLoader.loadedExtensionPackage.firstOrNull {
                    it::class.java.name == extensionPackage
                }?.loadedExtensionClasses?.get( extension )?.let {
                    entries += it to this
                }
            }
            isDataInitialized = true
        }
    }

}