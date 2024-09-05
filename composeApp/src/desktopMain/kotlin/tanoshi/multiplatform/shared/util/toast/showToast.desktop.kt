package tanoshi.multiplatform.shared.util.toast

import tanoshi.multiplatform.common.util.toast.ToastTimeout
import tanoshi.multiplatform.shared.SharedApplicationData

actual fun SharedApplicationData.showToast(
    message: String,
    timeout: ToastTimeout
) {
    toastDeque.push(
        message to if ( ToastTimeout.SHORT == timeout ) 1000L else 2000L
    )
    isToastWindowVisible = true
}
