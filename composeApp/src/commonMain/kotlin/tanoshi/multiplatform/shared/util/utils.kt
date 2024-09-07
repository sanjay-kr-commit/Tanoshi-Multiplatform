package tanoshi.multiplatform.shared.util

import androidx.compose.ui.graphics.ImageBitmap
import java.io.File

expect fun loadImageBitmap( file : File ) : ImageBitmap?