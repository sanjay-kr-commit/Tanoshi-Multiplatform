package tanoshi.multiplatform.android

import android.content.res.Configuration
import androidx.activity.ComponentActivity

interface DelegatedBehaviour {
    fun ComponentActivity.registerLifecycleOwner()
    fun ComponentActivity.registerLifecycleOwner( onCreateCallback : DelegatedBehaviourHandler.() -> Unit )
    fun extendOnConfigurationChanged( newConfig: Configuration , activitySpecific : ( newConfig: Configuration ) -> Unit = {} )
}