package tanoshi.multiplatform.desktop

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun TanoshiTheme( content : @Composable () -> Unit ) = MaterialTheme( content = content , colorScheme = darkColorScheme() )
