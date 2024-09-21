package tanoshi.multiplatform.common.extension.core

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.toast.ToastTimeout

// these objects are managed by and passed to dynamically loaded extension
open class SharedDependencies {
    var logger: Logger? = null
    var exportComposable
    : ((@Composable () -> Unit).()->Unit)? = null

    var showToast : (String.( ToastTimeout )->Unit)? = null

}