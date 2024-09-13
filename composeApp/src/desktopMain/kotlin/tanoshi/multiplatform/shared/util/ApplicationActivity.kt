package tanoshi.multiplatform.shared.util

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.desktop.util.WindowStack
import tanoshi.multiplatform.shared.SharedApplicationData

open class ApplicationActivity {

    lateinit var applicationData : SharedApplicationData
    lateinit var windowStack : WindowStack
    lateinit var composableActivityView : @Composable () -> Unit
    val isComposableViewSet : Boolean
        get() = this::composableActivityView.isInitialized

    open fun onCreate() {}

    open fun onPause() {}

    open fun onResume() {}

    open fun onDestroy() {}

    fun <applicationActivity:ApplicationActivity> changeActivity(
        applicationActivityName : Class<applicationActivity> ,
        vararg objects : Any
    ) = windowStack.add( applicationActivityName.getDeclaredConstructor().newInstance() )

    fun setContent(
        content: @Composable () -> Unit
    ) {
        composableActivityView = content
    }

}