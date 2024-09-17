package tanoshi.multiplatform.android.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import tanoshi.multiplatform.android.MyApplication
import tanoshi.multiplatform.android.extendOnConfigurationChangeBehaviour
import tanoshi.multiplatform.android.extendOnResumeBehaviour
import tanoshi.multiplatform.android.extendOncreateBehaviour
import tanoshi.multiplatform.common.extension.ExtensionPackage
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.model.BrowseScreenViewModel
import tanoshi.multiplatform.common.screens.BrowseScreen

class BrowseActivity : ComponentActivity() {

    private lateinit var sharedApplicationData : MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedApplicationData = extendOncreateBehaviour(savedInstanceState)
        val viewModel by viewModels<BrowseScreenViewModel>()
        viewModel.apply {
            extensionPackage = sharedApplicationData.exportedObjects?.get( "extensionPackage" ) as ExtensionPackage
            className = sharedApplicationData.exportedObjects?.get( "className" ) as String
            extension = sharedApplicationData.exportedObjects?.get( "extension" ) as Extension<*>
            sharedApplicationData.exportedObjects = null
        }
        setContent {
            BrowseScreen(
                viewModel , sharedApplicationData
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