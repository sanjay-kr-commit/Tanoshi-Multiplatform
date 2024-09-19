package tanoshi.multiplatform.android.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import dalvik.system.ZipPathValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tanoshi.multiplatform.android.MyApplication
import tanoshi.multiplatform.android.extendOnConfigurationChangeBehaviour
import tanoshi.multiplatform.android.extendOnResumeBehaviour
import tanoshi.multiplatform.android.extendOncreateBehaviour
import tanoshi.multiplatform.android.ui.theme.TanoshiTheme
import tanoshi.multiplatform.common.screens.SplashScreen
import tanoshi.multiplatform.common.util.ApplicationActivityName
import tanoshi.multiplatform.shared.changeActivity
import tanoshi.multiplatform.shared.util.toast.showToast
import java.io.File

class InitializeResources : ComponentActivity() {

    private var message = mutableStateOf( "Loading" )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extendOncreateBehaviour(savedInstanceState)
        setContent {
            TanoshiTheme {
                SplashScreen( message )
            }
        }
        initializeData()
    }

    private fun initializeData() = lifecycleScope.launch {
        with( application as MyApplication ) {

            delay( 500 )

            message.value = "Disable Zip Validator"
            disableZipValidator()
            delay( 10 )

            message.value = "Mapping Activity"
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

            message.value = "Attach Dynamic Activity To Extension Loader"
            extensionManager.extensionLoader.startDynamicActivity = {
                extensionComposableView = this as (@Composable () -> Unit )
                changeActivity = ApplicationActivityName.Dynamic
            }
            delay( 10 )

            message.value = "Attach Private Storage"
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

            message.value = "Attach Cache Storage"
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

            message.value = "Attach Internal Storage"
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

            message.value = "Attach Private Storage To Extension Manager"
            extensionManager.apply {
                dir = File( privateDir , "extensions" )
                cacheDir = this@with.appCacheDir
                classLoader = this@with.classLoader
            }
            delay( 10 )

            message.value = "Check For Storage"
            manageStorage = checkForStoragePermission()
            delay( 10 )

            message.value = "Attach Toast Lambda"
            showToastLambda = { message, timeout ->
                Toast.makeText( this@with , message , timeout ).show()
            }
            delay( 10 )

            message.value = "attach show toast to extension loader"
            extensionManager.extensionLoader.setToastLambda = {
                showToast( this , it )
            }
            delay( 10 )

            message.value = "Load Extensions"
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

        message.value = "Done"
        delay( 10 )

        startActivity( Intent( this@InitializeResources , MainActivity::class.java ) )
        finish()
    }

    override fun onResume() {
        super.onResume()
        extendOnResumeBehaviour()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        extendOnConfigurationChangeBehaviour(newConfig)
    }

    private fun disableZipValidator() {
        if (SDK_INT >= 34) {
            ZipPathValidator.clearCallback()
        }
    }

}