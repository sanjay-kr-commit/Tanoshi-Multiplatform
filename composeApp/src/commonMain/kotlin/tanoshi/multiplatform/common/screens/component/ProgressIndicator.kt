package tanoshi.multiplatform.common.screens.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator(
    color : Color = MaterialTheme.colorScheme.primary,
    size : Dp = 50.dp,
    modifier: Modifier = Modifier
) {
    val strokeSize = remember { ((size.value/200)*135).dp }
    CircularProgressIndicator( strokeWidth = strokeSize , color = color , modifier = modifier.size( size ))
}