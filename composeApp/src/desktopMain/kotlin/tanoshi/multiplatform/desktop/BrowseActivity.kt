package tanoshi.multiplatform.desktop

import tanoshi.multiplatform.common.extension.ExtensionPackage
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.model.BrowseScreenViewModel
import tanoshi.multiplatform.common.screens.BrowseScreen
import tanoshi.multiplatform.shared.util.ApplicationActivity

class BrowseActivity : ApplicationActivity() {

    override fun onCreate() {
        super.onCreate()
        val viewModel = BrowseScreenViewModel().apply {
            extensionPackage = applicationData.exportedObjects?.get( "extensionPackage" ) as ExtensionPackage
            className = applicationData.exportedObjects?.get( "className" ) as String
            extension = applicationData.exportedObjects?.get( "extension" ) as Extension<*>
            applicationData.exportedObjects = null
        }
        setContent {
            BrowseScreen( viewModel, applicationData )
        }
    }

}