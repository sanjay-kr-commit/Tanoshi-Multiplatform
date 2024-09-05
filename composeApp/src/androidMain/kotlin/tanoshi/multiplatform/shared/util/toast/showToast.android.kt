package tanoshi.multiplatform.shared.util.toast

import android.widget.Toast
import tanoshi.multiplatform.common.util.toast.ToastTimeout
import tanoshi.multiplatform.shared.SharedApplicationData

actual fun SharedApplicationData.showToast(
    message: String,
    timeout: ToastTimeout
) = showToastLambda( message , if ( ToastTimeout.SHORT == timeout ) Toast.LENGTH_SHORT else Toast.LENGTH_LONG )
