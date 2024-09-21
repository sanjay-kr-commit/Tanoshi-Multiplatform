package tanoshi.multiplatform.desktop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TanoshiTheme( darkColor : Boolean = true , content : @Composable () -> Unit ) = MaterialTheme( content = {
    Scaffold( Modifier.fillMaxSize() ) {
        content()
    } } , colorScheme = if ( darkColor ) darkColorScheme() else lightColorScheme()
)