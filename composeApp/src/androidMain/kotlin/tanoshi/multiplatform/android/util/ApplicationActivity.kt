package tanoshi.multiplatform.android.util

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import tanoshi.multiplatform.shared.SharedApplicationData

open class ApplicationActivity : ComponentActivity() {

    lateinit var sharedApplicationData : SharedApplicationData
    
    lateinit var saved : () -> Unit
    
    override fun onCreate(savedInstanceState: Bundle? ) {
        sharedApplicationData = application as SharedApplicationData
        sharedApplicationData._portrait = resources.configuration.orientation == 1
        sharedApplicationData.startCrashActivity = {}
        super.onCreate(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        sharedApplicationData._portrait = newConfig.orientation == 1
        super.onConfigurationChanged(newConfig)
    }

    override fun onResume() {
        super.onResume()
        sharedApplicationData.startCrashActivity = saved
    }
    
}

var ApplicationActivity.setCrashActivity : Class<*>
    get() = "This Variable is Used to assign Crash Activity" as Class<*>
    set(value) {
        sharedApplicationData.startCrashActivity = {
            finish()
            startActivity( Intent( this , value ) )
        }.also {
            saved = it
        }
        sharedApplicationData.logger log {
            DEBUG
            "Attached Crash Handling Activity : ${this@setCrashActivity::class.java} -> ${value.canonicalName}".replace( "class" , "" )
        }
    }
