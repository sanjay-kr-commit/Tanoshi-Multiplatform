package tanoshi.multiplatform.desktop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tanoshi.multiplatform.common.util.ApplicationActivityName
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.changeActivity
import tanoshi.multiplatform.shared.util.ApplicationActivity
import java.io.File

class InitializeResources : ApplicationActivity() {

    private var message by mutableStateOf( "Loading" )

    override fun onCreate() {
        super.onCreate()
        setContent {
            Scaffold(
                topBar = {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                bottomBar = {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            ) { it ->
                Column ( modifier = Modifier
                    .systemBarsPadding()
                    .padding( it )
                    .fillMaxSize(),
                    verticalArrangement = Arrangement.Center ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text( message )
                }
            }
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

            message = "Attack Private Path to Extension Manager"
            extensionManager.dir = File( privateDir , "extensions" )
            delay( 10 )

            message = "Attack Private Path to Extension Manager"
            extensionManager.logger = logger
            delay( 10 )

            message = "Attack Logger To Extension Loader"
            extensionManager.extensionLoader.logger = logger
            message = "Attack Private Path to Extension Manager"

            mapActivities()
            attachDynamicActivityLoading()

            message = "Loading Extension"
            extensionManager.loadExtensions()
            delay( 10 )

        }
        windowStack.add( MainActivity() )
        windowStack.remove( this@InitializeResources )
    }

    private fun SharedApplicationData.attachDynamicActivityLoading() {
        message = "Attach Start Dynamic Activity"
        extensionManager.extensionLoader.startDynamicActivity = {
            extensionComposableView = this as (@Composable () -> Unit )
            changeActivity = ApplicationActivityName.Dynamic
        }
    }

    private fun SharedApplicationData.mapActivities() {
        message = "Map Activities"
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