package tanoshi.multiplatform.shared.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tanoshi.multiplatform.desktop.util.WindowStack
import tanoshi.multiplatform.shared.SharedApplicationData

actual open class ApplicationActivity {
    
    lateinit var applicationData : SharedApplicationData
    lateinit var windowStack : WindowStack

    @Composable
    open fun onCreate() {
        Box( Modifier.fillMaxSize() , contentAlignment = Alignment.Center ) {
            Text( "Extend This Class For Creating Activities" )
        }
    }

    open fun onPause() {

    }

    open fun onResume() {}

    actual fun <applicationActivity:ApplicationActivity> changeActivity(
        applicationActivityName : Class<applicationActivity> ,
        vararg objects : Any
    ) = windowStack.add( applicationActivityName.getDeclaredConstructor().newInstance() )

}