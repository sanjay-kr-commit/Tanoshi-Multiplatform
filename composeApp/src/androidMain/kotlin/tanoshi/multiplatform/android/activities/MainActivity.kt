package tanoshi.multiplatform.android.activities

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import tanoshi.multiplatform.shared.SharedApplicationData

class MainActivity : ComponentActivity() {

    lateinit var sharedApplicaData : SharedApplicationData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedApplicaData = application as SharedApplicationData
        setContent {
            Text( "orientation : ${sharedApplicaData.portrait}" )
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        sharedApplicaData._portrait = newConfig.orientation == 1
        Toast.makeText( this , sharedApplicaData.portrait.toString() , Toast.LENGTH_SHORT ).show()
    }

}

@Preview
@Composable
fun AppAndroidPreview() {}