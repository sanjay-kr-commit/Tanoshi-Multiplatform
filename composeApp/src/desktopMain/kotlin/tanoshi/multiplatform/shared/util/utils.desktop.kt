package tanoshi.multiplatform.shared.util

import androidx.compose.ui.graphics.ImageBitmap
import java.io.File

actual fun loadImageBitmap(file: File): ImageBitmap? = try {
    androidx.compose.ui.res.loadImageBitmap( file.inputStream() )
} catch ( e : Exception ) {
    null
}