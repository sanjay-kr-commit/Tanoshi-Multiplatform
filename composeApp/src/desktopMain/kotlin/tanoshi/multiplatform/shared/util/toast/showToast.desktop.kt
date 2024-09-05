package tanoshi.multiplatform.shared.util.toast

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.util.toast.ToastTimeout
import tanoshi.multiplatform.shared.SharedApplicationData

actual fun SharedApplicationData.showToast(
    message: String,
    timeout: ToastTimeout
) {
    toastDeque.push(
        message to if ( ToastTimeout.SHORT == timeout ) 1000L else 2000L
    )
    if (toastJob == null) toastJob = CoroutineScope(Dispatchers.IO).launch {
        while (toastDeque.isNotEmpty()) {
            toastDeque.poll().let { toast ->
                isToastWindowVisible = true
                toastMessage = toast.first
                delay(toast.second)
                isToastWindowVisible = false
                delay( 100 )
            }
        }
        toastJob = null
    }
}
