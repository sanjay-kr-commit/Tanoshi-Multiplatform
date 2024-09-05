package tanoshi.multiplatform.shared.util.toast

import tanoshi.multiplatform.common.util.toast.ToastTimeout
import tanoshi.multiplatform.shared.SharedApplicationData

expect fun SharedApplicationData.showToast(
    message : String ,
    timeout : ToastTimeout
)