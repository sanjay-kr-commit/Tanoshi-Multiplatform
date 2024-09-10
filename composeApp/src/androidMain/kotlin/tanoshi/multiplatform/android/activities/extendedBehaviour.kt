package tanoshi.multiplatform.android.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import tanoshi.multiplatform.android.MyApplication

fun <Activity:ComponentActivity> Activity.extendOncreateBehaviour(savedInstanceState: Bundle?, extendedBehaviour : Activity.( MyApplication ) -> Unit = {} ) : MyApplication =
    (application as MyApplication).apply {
        _portrait = resources.configuration.orientation == 1
        extendedBehaviour( this )
    }


fun <Activity:ComponentActivity> Activity.extendOnConfigurationChangeBehaviour(newConfig: Configuration, extendedBehaviour : Activity.() -> Unit = {} ) {
    (application as MyApplication).apply {
        _portrait = newConfig.orientation == 1
    }
    extendedBehaviour()
}

fun <Activity:ComponentActivity> Activity.extendOnResumeBehaviour( extendedBehaviour: Activity.() -> Unit = {} ) {
    (application as MyApplication).apply {

    }
    extendedBehaviour()
}