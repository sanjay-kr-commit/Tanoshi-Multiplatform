package tanoshi.multiplatform.shared.util

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    if ( enabled ) Button( onClick = onBack ) {
        Text("Back")
    }
}