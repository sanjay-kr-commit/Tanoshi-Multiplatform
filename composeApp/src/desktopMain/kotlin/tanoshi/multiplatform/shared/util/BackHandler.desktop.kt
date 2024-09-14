package tanoshi.multiplatform.shared.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
actual fun BackHandler(enabled: Boolean , onBack: () -> Unit) {
    AnimatedVisibility( enabled , enter = slideIn( initialOffset = {
        IntOffset(
            0,
            it.height+1
        )
    } ) , exit = slideOut( targetOffset = {
        IntOffset(
            0,
            it.height+1
        )
    } ) ) {
        Box( Modifier.padding( 5.dp ).wrapContentSize().clip( RoundedCornerShape( 10.dp ) ).clickable( onClick = onBack ).background( Color.Gray ).padding( 5.dp ) , contentAlignment = Alignment.Center ) {
            Text( "Go Back" , color = Color.White )
        }
    }
}