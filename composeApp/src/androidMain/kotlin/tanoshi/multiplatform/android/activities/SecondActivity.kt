package tanoshi.multiplatform.android.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import org.jetbrains.compose.components.resources.BuildConfig
import tanoshi.multiplatform.shared.util.ApplicationActivity
import tanoshi.multiplatform.shared.util.setCrashActivity

class SecondActivity : ApplicationActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCrashActivity = CrashHandlingActivity::class.java
        if ( checkSelfPermission( Manifest.permission.MANAGE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_DENIED ) sharedApplicationData.logger log {
            ERROR
            title = "Storage Permission Not Granted"
            title
        } else sharedApplicationData.logger log  {
            DEBUG
            title = "Storage Permission Granted"
            title
        }
        try {
        } catch ( e : Exception ) {
        }
        setContent {
            Column( verticalArrangement = Arrangement.Center , modifier = Modifier.fillMaxSize() ) {
                val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                    isGranted ->
                    Toast.makeText( this@SecondActivity , "Granted = $isGranted" , Toast.LENGTH_LONG ).show()
                }
                Text( "Second Acitivity" )
                Button( {
                    startActivity(
                        Intent(
                            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION ,
                            Uri.parse("package:${BuildConfig.LIBRARY_PACKAGE_NAME}")
                        )
                    )
                    if ( checkSelfPermission( Manifest.permission.MANAGE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_DENIED ) sharedApplicationData.logger log {
                        ERROR
                        title = "Storage Permission Not Granted"
                        title
                    } else sharedApplicationData.logger log  {
                        DEBUG
                        title = "Storage Permission Granted"
                        title
                    }
                } ) {
                    Text( "Portrait : ${sharedApplicationData?.portrait}" )
                }
                Button( {
                    val i = Intent( this@SecondActivity , MainActivity::class.java )
                    startActivity( i )
                } ) {
                    Text( "Change Activity" )
                }
            }
        }
    }

}