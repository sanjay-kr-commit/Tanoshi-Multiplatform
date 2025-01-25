package tanoshi.multiplatform.android.activities

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import tanoshi.multiplatform.android.DelegatedBehaviour
import tanoshi.multiplatform.android.DelegatedBehaviourHandler
import tanoshi.multiplatform.android.MyApplication
import tanoshi.multiplatform.android.ui.theme.TanoshiTheme
import tanoshi.multiplatform.common.model.MainScreenViewModel
import tanoshi.multiplatform.common.screens.MainScreen
import java.util.*

class MainActivity : ComponentActivity() , DelegatedBehaviour by DelegatedBehaviourHandler() {

    private val onResumeTask : Stack<()->Unit> = Stack()
    private lateinit var sharedApplicationData : MyApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedApplicationData = application as MyApplication
        registerLifecycleOwner {
            extendOnResume = {
                while ( onResumeTask.isNotEmpty() ) onResumeTask.pop().invoke()
            }
        }
        val mainScreenViewModel by viewModels<MainScreenViewModel>()
        setContent {
            TanoshiTheme {
                when {
                    !sharedApplicationData.manageStorage -> RequestStoragePermission()
                    else -> Column(
                        modifier = Modifier.fillMaxSize()
                            .systemBarsPadding()
                    ) {
                        MainScreen(
                            sharedApplicationData ,
                            mainScreenViewModel
                        )
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        extendOnConfigurationChanged(newConfig)
    }

    @Composable
    fun RequestStoragePermission() {
        Box( modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center ) {
            Button( onClick = {
                requestPermission()
                onResumeTask.push {
                    sharedApplicationData.manageStorage = sharedApplicationData.checkForStoragePermission()
                }
            } ) {
                Text( "Storage Permission Required" )
            }
        }

    }

    @Suppress("DEPRECATION")
    private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent: Intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.setData(
                    Uri.parse(
                        String.format(
                            "package:%s",
                            applicationContext.packageName
                        )
                    )
                )
                startActivityForResult(intent, 2296)
            } catch (e: Exception) {
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, 2296)
            }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    WRITE_EXTERNAL_STORAGE
                ), 0
            )
        }
    }

}