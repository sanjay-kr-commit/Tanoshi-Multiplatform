package tanoshi.multiplatform.common.extension.core

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.util.logger.Logger

// these objects are managed by and passed to dynamically loaded extension
open class SharedDependencies {
    lateinit var logger: Logger
    var startComposableView : ((@Composable () -> Unit).()->Unit)? = null
}