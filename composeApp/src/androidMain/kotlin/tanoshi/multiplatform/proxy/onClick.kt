package androidx.compose.foundation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun Modifier.onClick(
    enabled: Boolean = true ,
    onClick : () -> Unit
) : Modifier = clickable(
    enabled = enabled ,
    onClick = onClick
)