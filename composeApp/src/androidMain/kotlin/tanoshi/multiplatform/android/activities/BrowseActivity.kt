package tanoshi.multiplatform.android.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import tanoshi.multiplatform.android.MyApplication
import tanoshi.multiplatform.android.extendOnConfigurationChangeBehaviour
import tanoshi.multiplatform.android.extendOnResumeBehaviour
import tanoshi.multiplatform.android.extendOncreateBehaviour
import tanoshi.multiplatform.common.screens.BrowseScreen

class BrowseActivity : ComponentActivity() {

    private lateinit var sharedApplicationData : MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedApplicationData = extendOncreateBehaviour(savedInstanceState)
        setContent {
            BrowseScreen(
                sharedApplicationData
            )
        }
    }

    override fun onResume() {
        super.onResume()
        extendOnResumeBehaviour()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        extendOnConfigurationChangeBehaviour(newConfig)
    }

}