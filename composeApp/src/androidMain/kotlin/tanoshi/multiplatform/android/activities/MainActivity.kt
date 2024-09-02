package tanoshi.multiplatform.android.activities

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import tanoshi.multiplatform.common.model.MainScreenViewModel
import tanoshi.multiplatform.common.screens.MainScreen
import tanoshi.multiplatform.shared.util.ApplicationActivity
import tanoshi.multiplatform.shared.util.setCrashActivity

class MainActivity : ApplicationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCrashActivity = CrashHandlingActivity::class.java
        val mainScreenViewModel by viewModels<MainScreenViewModel>()
        setContent {
            when {
                !sharedApplicationData.manageStorage -> RequestStoragePermission()
                else -> Column {
                    Spacer( Modifier.height( 20.dp ) )
                    MainScreen(
                        sharedApplicationData ,
                        mainScreenViewModel
                    )
                }
            }
        }
    }

    @Composable
    fun RequestStoragePermission() {
        Box( modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center ) {
            Button( onClick = {
                requestPermission()
                sharedApplicationData.manageStorage = sharedApplicationData.checkForStoragePermission()
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