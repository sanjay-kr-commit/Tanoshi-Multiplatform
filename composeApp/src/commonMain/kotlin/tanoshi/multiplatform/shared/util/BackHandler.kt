package tanoshi.multiplatform.shared.util

import androidx.compose.runtime.Composable

@Composable
expect fun  BackHandler(enabled: Boolean = true, onBack: () -> Unit)