package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.screens.component.DesktopOnlyBackHandler
import tanoshi.multiplatform.common.util.Platform
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.finish
import tanoshi.multiplatform.shared.util.BackHandler
import tanoshi.multiplatform.shared.util.PLATFORM

@Composable
fun BrowseScreen(
    sharedData : SharedApplicationData
) {
    Scaffold(
        bottomBar = {
            DesktopOnlyBackHandler( sharedData )
        }
    ) {
        Box( Modifier.padding(it).fillMaxSize() , contentAlignment = Alignment.Center ) {
            Text( "Browse Screen" )
        }
    }
}