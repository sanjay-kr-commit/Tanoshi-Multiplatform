package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.screens.component.ProgressIndicator

@Composable
fun SplashScreen(
    message : MutableState<String>
) {

    Scaffold(
        topBar = {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        },
        bottomBar = {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { it ->
        Column ( modifier = Modifier
            .systemBarsPadding()
            .padding( it )
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicator()
            Spacer( Modifier.height( 10.dp ) )
            Text( message.value )
        }
    }

}