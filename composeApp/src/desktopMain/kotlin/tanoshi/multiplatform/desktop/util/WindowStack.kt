package tanoshi.multiplatform.desktop.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.util.ApplicationActivity

class WindowStack(
    startActivity : ApplicationActivity ,
    private val sharedApplicationData : SharedApplicationData
) {

    private var _activeWindow : MutableState<ApplicationActivity> = mutableStateOf( startActivity )
    val activeWindow : ApplicationActivity
        get() = _activeWindow.value

    init {
        startActivity.onCreate()
        startActivity.windowStack = this
        startActivity.applicationData = sharedApplicationData
    }
    
    private var stack : ArrayList<ApplicationActivity> = arrayListOf(
        startActivity
    )

    fun add( activity : ApplicationActivity ) {
        activity.windowStack = this
        activity.applicationData = sharedApplicationData
        activity.onCreate()
        stack.add( activity )
        _activeWindow.value.onPause()
        _activeWindow.value = activity
    }

    fun remove( activity: ApplicationActivity ) {
        activity.onDestroy()
        stack.remove( activity )
        if ( _activeWindow != stack.last() ) {
            _activeWindow.value = stack.last()
            _activeWindow.value.onResume()
        }
    }

    fun pop() {
        remove( stack.last() )
    }

    @Composable
    fun render() {
        stack.forEach { activity ->
            AnimatedVisibility( activity == _activeWindow.value , modifier = Modifier.fillMaxSize() , enter = fadeIn() , exit = fadeOut() ) {
                if (_activeWindow.value.isComposableViewSet) _activeWindow.value.composableActivityView()
                else Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Set Composable To using setComposableContent function inside onCreate")
                }
            }
        }
    }

    companion object {
        @Composable
        infix fun SharedApplicationData.startWindowStack( applicationActivity: ApplicationActivity ) {
            remember {
                windowStack = WindowStack( applicationActivity , this )
                    .also {
                        logger log {
                            DEBUG
                            title = "Initialised Window Stack"
                            title
                        }
                    }
            }
            windowStack.render()
        }
    }

}