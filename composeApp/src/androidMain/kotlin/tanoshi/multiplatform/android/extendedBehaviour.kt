package tanoshi.multiplatform.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

fun <Activity:ComponentActivity> Activity.extendOncreateBehaviour(savedInstanceState: Bundle?, extendedBehaviour : Activity.( MyApplication ) -> Unit = {} ) : MyApplication =
    (application as MyApplication).apply {
        enableEdgeToEdge()
        _portrait = resources.configuration.orientation == 1
        exitActivity = {
            finish()
        }
        extendedBehaviour( this )
    }


fun <Activity:ComponentActivity> Activity.extendOnConfigurationChangeBehaviour(newConfig: Configuration, extendedBehaviour : Activity.() -> Unit = {} ) {
    (application as MyApplication).apply {
        exitActivity = {
            finish()
        }
        _portrait = newConfig.orientation == 1
    }
    extendedBehaviour()
}

fun <Activity:ComponentActivity> Activity.extendOnResumeBehaviour( extendedBehaviour: Activity.() -> Unit = {} ) {
    (application as MyApplication).apply {
        exitActivity = {
            finish()
        }
    }
    extendedBehaviour()
}