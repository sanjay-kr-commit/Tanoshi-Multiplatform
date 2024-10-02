package tanoshi.multiplatform.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.db.Library
import tanoshi.multiplatform.common.db.Preferences.initializePreference
import tanoshi.multiplatform.common.screens.SplashScreen
import tanoshi.multiplatform.common.util.ApplicationActivityName
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.changeActivity
import tanoshi.multiplatform.shared.util.ApplicationActivity
import tanoshi.multiplatform.shared.util.toast.showToast
import java.io.File

class InitializeResources : ApplicationActivity() {

    private var message = mutableStateOf( "Loading" )

    override fun onCreate() {
        super.onCreate()
        setContent {
            SplashScreen( message )
        }
        initializeData()
    }

    private fun initializeData() = CoroutineScope( Dispatchers.Default ).launch {

        delay( 500 )

        with( applicationData ) {

            logger log {
                title = "App Start Up Time"
                "App Started At $appStartUpTime"
            }

            logger log {
                title = "App Directory"
                publicDir.absolutePath
            }

            message.value = "Attack Private Path to Extension Manager"
            extensionManager.dir = File( privateDir , "extensions" )
            delay( 10 )

            message.value = "Attack Private Path to Extension Manager"
            extensionManager.logger = logger
            delay( 10 )

            message.value = "Attack Logger To Extension Loader"
            extensionManager.extensionLoader.logger = logger
            message.value = "Attack Private Path to Extension Manager"

            mapActivities()
            attachDynamicActivityLoading()

            message.value = "attach show toast to extension loader"
            extensionManager.extensionLoader.setToastLambda = {
                showToast( this , it )
            }
            delay( 10 )

            message.value = "Loading Library"
            _library_ = Library( privateDir )
            delay( 10 )

            message.value = "Loading Preferences"
            initializePreference( privateDir )
            delay( 10 )

            message.value = "Loading Extension"
            extensionManager.loadExtensions()
            delay( 10 )

        }
        windowStack.add( MainActivity() )
        windowStack.remove( this@InitializeResources )
    }

    private fun SharedApplicationData.attachDynamicActivityLoading() {
        message.value = "Attach Start Dynamic Activity"
        extensionManager.extensionLoader.startDynamicActivity = {
            extensionComposableView = this as (@Composable () -> Unit )
            changeActivity = ApplicationActivityName.Dynamic
        }
    }

    private fun SharedApplicationData.mapActivities() {
        message.value = "Map Activities"
        activityMap = mapOf(
            ApplicationActivityName.Main to {
                windowStack.add( MainActivity::class.java.getDeclaredConstructor().newInstance() )
            } ,
            ApplicationActivityName.Browse to {
                windowStack.add( BrowseActivity::class.java.getDeclaredConstructor().newInstance() )
            } ,
            ApplicationActivityName.Dynamic to {
                windowStack.add( DynamicActivity::class.java.getDeclaredConstructor().newInstance() )
            }
        )
    }

}