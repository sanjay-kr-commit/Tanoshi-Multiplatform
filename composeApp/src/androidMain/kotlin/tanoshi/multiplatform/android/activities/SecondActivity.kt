package tanoshi.multiplatform.android.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import tanoshi.multiplatform.android.util.ApplicationActivity
import tanoshi.multiplatform.android.util.setCrashActivity

class SecondActivity : ApplicationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCrashActivity = CrashActivity::class.java
        setContent {
            Column( verticalArrangement = Arrangement.Center , modifier = Modifier.fillMaxSize() ) {
                Text( "Second Acitivity" )
                Button( {
                    throw Exception( "\n\n\n\nWhatever" )
                } ) {
                    Text( "Portrait : ${sharedApplicationData?.portrait}" )
                }
                Button( {
                    Intent( this@SecondActivity , MainActivity::class.java )
                } ) {
                    Text( "Change Activity" )
                }
            }
        }
    }

}