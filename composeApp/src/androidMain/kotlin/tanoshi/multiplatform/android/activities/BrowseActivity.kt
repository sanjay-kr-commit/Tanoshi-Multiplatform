package tanoshi.multiplatform.android.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import tanoshi.multiplatform.android.*
import tanoshi.multiplatform.android.ui.theme.TanoshiTheme
import tanoshi.multiplatform.common.extension.ExtensionPackage
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.model.BrowseScreenViewModel
import tanoshi.multiplatform.common.screens.BrowseScreen

class BrowseActivity : ComponentActivity() , DelegatedBehaviour by DelegatedBehaviourHandler() {

    private lateinit var sharedApplicationData : MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedApplicationData = application as MyApplication
        registerLifecycleOwner()
        val viewModel by viewModels<BrowseScreenViewModel>()
        viewModel.apply {
            extensionPackage = sharedApplicationData.exportedObjects?.get( "extensionPackage" ) as ExtensionPackage
            className = sharedApplicationData.exportedObjects?.get( "className" ) as String
            extension = sharedApplicationData.exportedObjects?.get( "extension" ) as Extension<*>
            sharedApplicationData.exportedObjects = null
        }
        setContent {
            TanoshiTheme {
                BrowseScreen(
                    viewModel , sharedApplicationData
                )
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        extendOnConfigurationChanged(newConfig)
    }

}