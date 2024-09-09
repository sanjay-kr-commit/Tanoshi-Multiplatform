package tanoshi.multiplatform.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext

expect open class ApplicationActivity {

    fun <applicationActivity:ApplicationActivity> changeActivity(
        applicationActivityName : Class<applicationActivity> ,
        vararg objects : Any
    )

    fun setComposableContent(
        parent: CompositionContext? = null,
        content: @Composable () -> Unit
    )

}