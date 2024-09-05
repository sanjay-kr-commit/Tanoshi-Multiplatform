package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.onClick
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tanoshi.multiplatform.common.util.toast.ToastTimeout
import tanoshi.multiplatform.shared.SharedApplicationData
import tanoshi.multiplatform.shared.util.toast.showToast

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    sharedAppData : SharedApplicationData
) {
    Box(modifier = Modifier.fillMaxSize()
        .onClick {
            sharedAppData.showToast(
                "hello" , ToastTimeout.SHORT
            )
        }
    ) {
        Text( "Home" )
    }
}