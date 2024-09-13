package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.awt.SplashScreen

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
            Text( message.value )
        }
    }

}