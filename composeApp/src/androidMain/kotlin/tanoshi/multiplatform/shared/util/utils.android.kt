package tanoshi.multiplatform.shared.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File

actual fun loadImageBitmap(file: File): ImageBitmap? = try {
    BitmapFactory.decodeStream(file.inputStream())
        .asImageBitmap()
} catch ( e : Exception ) {
    null
}