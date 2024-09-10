package tanoshi.multiplatform.desktop

import tanoshi.multiplatform.common.screens.BrowseScreen
import tanoshi.multiplatform.shared.util.ApplicationActivity

class BrowseActivity : ApplicationActivity() {

    override fun onCreate() {
        super.onCreate()
        setContent {
            BrowseScreen( applicationData )
        }
    }

}