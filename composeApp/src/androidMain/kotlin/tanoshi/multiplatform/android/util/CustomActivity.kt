package tanoshi.multiplatform.android.util

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import tanoshi.multiplatform.shared.SharedApplicationData

open class ApplicationActivity : ComponentActivity() {

    var sharedApplicationData : SharedApplicationData? = null
    
    lateinit var saved : () -> Unit
    
    override fun onCreate(savedInstanceState: Bundle? ) {
        if ( application is SharedApplicationData ) sharedApplicationData = application as SharedApplicationData
        sharedApplicationData?._portrait = requestedOrientation == 1
        sharedApplicationData?.startCrashActivity = {}
        super.onCreate(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        sharedApplicationData?._portrait = newConfig.orientation == 1
        super.onConfigurationChanged(newConfig)
    }

    override fun onResume() {
        super.onResume()
        sharedApplicationData?.startCrashActivity = saved
    }
    
}

var ApplicationActivity.setCrashActivity : Class<*>
    get() = "This Variable is Used to assign Crash Activity" as Class<*>
    set(value) {
        sharedApplicationData?.startCrashActivity = {
            finish()
            startActivity( Intent( this , value ) )
        }.also {
            saved = it
        }
    }
