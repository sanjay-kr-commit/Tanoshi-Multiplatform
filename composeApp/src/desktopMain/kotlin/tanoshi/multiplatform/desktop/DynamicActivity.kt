package tanoshi.multiplatform.desktop

import tanoshi.multiplatform.shared.util.ApplicationActivity

class DynamicActivity : ApplicationActivity() {

    override fun onCreate() {
        super.onCreate()
        setContent {
            TanoshiTheme {
                applicationData.extensionComposableView
            }
        }
    }
}