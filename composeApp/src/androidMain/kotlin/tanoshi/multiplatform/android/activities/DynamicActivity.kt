package tanoshi.multiplatform.android.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import tanoshi.multiplatform.android.*
import tanoshi.multiplatform.android.ui.theme.TanoshiTheme

class DynamicActivity : ComponentActivity() , DelegatedBehaviour by DelegatedBehaviourHandler() {

    private lateinit var sharedApplicationData : MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedApplicationData = application as MyApplication
        registerLifecycleOwner()
        setContent {
            TanoshiTheme {
                Scaffold {
                    sharedApplicationData.extensionComposableView()
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        extendOnConfigurationChanged(newConfig)
    }

}