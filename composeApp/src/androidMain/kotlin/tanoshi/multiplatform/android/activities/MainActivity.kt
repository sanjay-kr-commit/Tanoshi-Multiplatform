package tanoshi.multiplatform.android.activities

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hi : ArrayList<String>? = null
        var yaahooo by mutableStateOf( "Yahoo" )
        setContent {
            Button(
                onClick =  {
                    yaahooo += "yahoo "
                    hi!!.add( "Hello" )
                }
            ) {
                Text( yaahooo )
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {}