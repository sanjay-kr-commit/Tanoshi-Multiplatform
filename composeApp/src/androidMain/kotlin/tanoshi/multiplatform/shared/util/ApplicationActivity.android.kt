package tanoshi.multiplatform.shared.util

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import tanoshi.multiplatform.android.MyApplication

actual open class ApplicationActivity : ComponentActivity() {

    lateinit var sharedApplicationData : MyApplication
    lateinit var saved : () -> Unit
    
    override fun onCreate(savedInstanceState: Bundle? ) {
        sharedApplicationData = application as MyApplication
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

    actual fun <applicationActivity:ApplicationActivity> changeActivity(
        applicationActivityName : Class<applicationActivity> ,
        vararg objects : Any
    ) {
        val intent = Intent( this@ApplicationActivity , applicationActivityName )
        startActivity( intent )
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
            title = "Attached Crash Handling Activity"
            "${this@setCrashActivity::class.java} -> ${value.canonicalName}".replace( "class" , "" )
        }
    }
