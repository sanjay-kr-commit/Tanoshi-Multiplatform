package tanoshi.multiplatform.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import tanoshi.multiplatform.desktop.util.WindowStack
import tanoshi.multiplatform.shared.SharedApplicationData

actual open class ApplicationActivity {
    
    lateinit var applicationData : SharedApplicationData
    lateinit var windowStack : WindowStack
    lateinit var composableActivityView : @Composable () -> Unit
    val isComposableViewSet : Boolean
        get() = this::composableActivityView.isInitialized

    open fun onCreate() {}

    open fun onPause() {}

    open fun onResume() {}

    actual fun <applicationActivity:ApplicationActivity> changeActivity(
        applicationActivityName : Class<applicationActivity> ,
        vararg objects : Any
    ) = windowStack.add( applicationActivityName.getDeclaredConstructor().newInstance() )

    actual fun setComposableContent(
        parent: CompositionContext?,
        content: @Composable () -> Unit
    ) {
        composableActivityView = content
    }

}