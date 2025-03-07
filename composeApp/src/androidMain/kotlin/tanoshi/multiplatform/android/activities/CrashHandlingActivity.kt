package tanoshi.multiplatform.android.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.android.*
import tanoshi.multiplatform.android.ui.theme.TanoshiTheme
import tanoshi.multiplatform.common.screens.LogScreen
import tanoshi.multiplatform.shared.SharedApplicationData

class CrashHandlingActivity : ComponentActivity() , DelegatedBehaviour by DelegatedBehaviourHandler() {

    private lateinit var sharedApplicaData : SharedApplicationData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedApplicaData = application as SharedApplicationData
        registerLifecycleOwner()
        setContent {
            TanoshiTheme {
                Column( Modifier.fillMaxSize() ) {
                    Spacer( Modifier.height( 20.dp ) )
                    LogScreen(
                        sharedApplicaData.logger
                    )
                    BackHandler {
                        finishAffinity()
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        extendOnConfigurationChanged(newConfig)
    }

}