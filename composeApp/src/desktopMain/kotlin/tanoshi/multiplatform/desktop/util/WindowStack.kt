package tanoshi.multiplatform.desktop.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
        _activeWindow.value = activity
    }
    

    fun addAll( vararg activities : ApplicationActivity ) {
        activities.forEach { activity ->
            add( activity )
        }
        _activeWindow.value = stack.last()
    }

    fun remove( activity: ApplicationActivity ) {
        stack.remove( activity )
        _activeWindow.value = stack.last()
    }

    fun removeAll( vararg activities : ApplicationActivity ) {
        stack.removeAll(activities.toSet())
        _activeWindow.value = stack.last()
    }

    @Composable
    fun render() {
        if ( _activeWindow.value.isComposableViewSet ) _activeWindow.value.composableActivityView()
        else Box( Modifier.fillMaxSize() , contentAlignment = Alignment.Center ) {
            Text( "Set Composable To using setComposableContent function inside onCreate" )
        }
    }

}