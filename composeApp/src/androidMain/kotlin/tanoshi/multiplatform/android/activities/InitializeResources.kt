package tanoshi.multiplatform.android.activities

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.lifecycleScope
import dalvik.system.ZipPathValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tanoshi.multiplatform.android.MyApplication
import tanoshi.multiplatform.common.util.ApplicationActivityName
import tanoshi.multiplatform.shared.changeActivity
import java.io.File

class InitializeResources : ComponentActivity() {

    private var message by mutableStateOf( "Loading" )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun initializeData() = lifecycleScope.launch {
        with( application as MyApplication ) {

            delay( 500 )

            message = "Disable Zip Validator"
            disableZipValidator()
            delay( 10 )

            message = "Mapping Activity"
            activityMap = mapOf(
                ApplicationActivityName.Main to {
                    startActivity( Intent( this , MainActivity::class.java ).apply {
                        flags = Intent.FLAG_FROM_BACKGROUND
                        flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                } ,
                ApplicationActivityName.Browse to {
                    startActivity( Intent( this , BrowseActivity::class.java ).apply {
                        flags = Intent.FLAG_FROM_BACKGROUND
                        flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                } ,
                ApplicationActivityName.Dynamic to {
                    startActivity( Intent( this , DynamicActivity::class.java ).apply {
                        flags = Intent.FLAG_FROM_BACKGROUND
                        flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
            )
            delay( 10 )

            message = "Attach Dynamic Activity To Extension Loader"
            extensionManager.extensionLoader.startDynamicActivity = {
                extensionComposableView = this as (@Composable () -> Unit )
                changeActivity = ApplicationActivityName.Dynamic
            }
            delay( 10 )

            message = "Attach Private Storage"
            try {
                privateDir = getDir( "tanoshi" , MODE_PRIVATE )
                logger log {
                    title = "Private Storage Path"
                    privateDir.absolutePath
                }
            } catch ( e : Exception ) {
                logger log {
                    title = "Cannot Access App Internal Storage Path"
                    e.stackTraceToString()
                }
            }
            delay( 10 )

            message = "Attach Cache Storage"
            try {
                appCacheDir = getCacheDir()
                logger log {
                    title = "Cache Storage Path"
                    appCacheDir.absolutePath
                }
            } catch ( e : Exception ) {
                logger log {
                    title = "Cannot Access App Cache Storage Path"
                    e.stackTraceToString()
                }
            }
            delay( 10 )

            message = "Attach Internal Storage"
            try {
                publicDir = Environment.getExternalStorageDirectory()
                logger log {
                    title = "Internal Storage Path"
                    publicDir.absolutePath
                }
            } catch ( e : Exception ) {
                logger log {
                    title = "Cannot Access Internal Storage Path"
                    e.stackTraceToString()
                }
            }
            delay( 10 )

            message = "Attach Private Storage To Extension Manager"
            extensionManager.apply {
                dir = File( privateDir , "extensions" )
                cacheDir = this@with.appCacheDir
                classLoader = this@with.classLoader
            }
            delay( 10 )

            message = "Check For Storage"
            manageStorage = checkForStoragePermission()
            delay( 10 )

            message = "Attach Toast Lambda"
            showToastLambda = { message , timeout ->
                Toast.makeText( this@with , message , timeout ).show()
            }
            delay( 10 )

            message = "Load Extensions"
            try {
                extensionManager.loadExtensions()
            } catch ( e : Exception ) {
                logger log  {
                    ERROR
                    title = "Failed To Load Extensions"
                    e.stackTraceToString()
                }
            }
            delay( 10 )

        }

        message = "Done"
        delay( 10 )

        startActivity( Intent( this@InitializeResources , MainActivity::class.java ) )
        finish()
    }


    private fun disableZipValidator() {
        if (SDK_INT >= 34) {
            ZipPathValidator.clearCallback()
        }
    }

}