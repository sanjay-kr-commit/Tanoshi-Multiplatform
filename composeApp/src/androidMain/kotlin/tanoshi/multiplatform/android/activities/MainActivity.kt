package tanoshi.multiplatform.android.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.shared.util.ApplicationActivity
import tanoshi.multiplatform.shared.util.setCrashActivity
import tanoshi.multiplatform.common.model.MainScreenViewModel
import tanoshi.multiplatform.common.screens.MainScreen

class MainActivity : ApplicationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCrashActivity = CrashActivity::class.java
        val mainScreenViewModel by viewModels<MainScreenViewModel>()
        setContent {
            Column {
                Spacer( Modifier.height( 20.dp ) )
                MainScreen(
                    sharedApplicationData ,
                    mainScreenViewModel
                )
            }
        }
    }

}

@Preview
@Composable
fun AppAndroidPreview() {}