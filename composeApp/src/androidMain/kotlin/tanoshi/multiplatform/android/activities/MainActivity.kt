package tanoshi.multiplatform.android.activities

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import tanoshi.multiplatform.common.model.MainScreenViewModel
import tanoshi.multiplatform.common.screens.MainScreen
import tanoshi.multiplatform.shared.util.ApplicationActivity
import tanoshi.multiplatform.shared.util.setCrashActivity
import java.util.Stack

class MainActivity : ApplicationActivity() {

    private val onResumeTask : Stack<()->Unit> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCrashActivity = CrashHandlingActivity::class.java
        val mainScreenViewModel by viewModels<MainScreenViewModel>()
        setComposableContent {
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

    override fun onResume() {
        super.onResume()
        while ( onResumeTask.isNotEmpty() ) onResumeTask.pop().invoke()
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