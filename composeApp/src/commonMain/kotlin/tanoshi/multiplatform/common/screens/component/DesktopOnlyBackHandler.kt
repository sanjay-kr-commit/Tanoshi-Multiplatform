package tanoshi.multiplatform.common.screens.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.util.Platform
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.finish
import tanoshi.multiplatform.shared.util.BackHandler
import tanoshi.multiplatform.shared.util.PLATFORM

@Composable
fun DesktopOnlyBackHandler(
    sharedData : SharedApplicationData
) {
    if ( PLATFORM == Platform.Desktop ) Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        horizontalArrangement = Arrangement.End
    ) {
        var enableVisibility by remember { mutableStateOf(false) }
        BackHandler(enableVisibility) {
            CoroutineScope( Dispatchers.Default ).launch {
                enableVisibility = false
                delay( 100 )
                sharedData.finish
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        LaunchedEffect(Unit) {
            enableVisibility = true
        }
    }
}