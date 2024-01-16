package androidx.compose.foundation

import androidx.compose.ui.Modifier

fun Modifier.onClick(
    enabled: Boolean = true ,
    onClick : () -> Unit
) : Modifier = clickable(
    enabled = enabled ,
    onClick = onClick
)