package tanoshi.multiplatform.android

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class DelegatedBehaviourHandler : LifecycleEventObserver , DelegatedBehaviour {

    lateinit var extendOnResume : () -> Unit
    lateinit var extendOnPause : () -> Unit
    lateinit var extendOnStart : () -> Unit
    lateinit var extendOnStop : () -> Unit
    lateinit var extendOnDestroy : () -> Unit
    lateinit var extendOnAny : () -> Unit
    private lateinit var application : MyApplication
    private lateinit var activity : ComponentActivity

    override fun ComponentActivity.registerLifecycleOwner() {
        this@DelegatedBehaviourHandler.let { delegate ->
            delegate.application = application as MyApplication
            delegate.activity = this@registerLifecycleOwner
        }
        lifecycle.addObserver(this@DelegatedBehaviourHandler )
    }

    override fun ComponentActivity.registerLifecycleOwner( onCreateCallback : DelegatedBehaviourHandler.() -> Unit) {
        registerLifecycleOwner()
        this@DelegatedBehaviourHandler.onCreateCallback()
    }

    override fun extendOnConfigurationChanged( newConfig: Configuration , activitySpecific : ( newConfig: Configuration ) -> Unit ) {
        application.run {
            exitActivity = {
                activity.finish()
            }
            _portrait = newConfig.orientation == 1
        }
        activitySpecific(newConfig)
    }

    private fun onCreate() {
    }

    private fun onPause() {
        if ( this::extendOnPause.isInitialized ) extendOnPause()
    }

    private fun onResume () {
        application.run {
            exitActivity = {
                activity.finish()
            }
        }
        if ( this::extendOnResume.isInitialized ) extendOnResume()
    }

    private fun onStart() {
        if ( this::extendOnStart.isInitialized ) extendOnStart()
    }

    private fun onStop() {
        if ( this::extendOnStop.isInitialized ) extendOnStop()
    }

    private fun onDestroy() {
        if ( this::extendOnDestroy.isInitialized ) extendOnDestroy()
    }

    private fun onAny() {
        if ( this::extendOnAny.isInitialized ) extendOnAny()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate()
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_STOP -> onStop()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            Lifecycle.Event.ON_ANY -> onAny()
        }
    }

}