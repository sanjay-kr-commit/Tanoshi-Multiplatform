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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
actual fun BackHandler(enabled: Boolean , onBack: () -> Unit) {
    AnimatedVisibility(
        enabled ,
        enter = slideIn( initialOffset = {IntOffset(0,it.height+1 )} ) ,
        exit = slideOut( targetOffset = {IntOffset(0,it.height+1 )} )
    ) {
        val uniqueId by remember { mutableStateOf( BackHandlerBackStack.attach ) }
        AnimatedVisibility(
            uniqueId == BackHandlerBackStack.activeValue ,
            enter = slideIn( initialOffset = {IntOffset(0,it.height+1 )} ) ,
            exit = slideOut( targetOffset = {IntOffset(0,it.height+1 )} )
        ) {
            Box( modifier = Modifier
                .padding( 5.dp )
                .wrapContentSize()
                .clip( RoundedCornerShape( 10.dp ) )
                .clickable( onClick = onBack )
                .background( MaterialTheme.colorScheme.onPrimaryContainer.copy(0.2f) )
                .padding( 5.dp ) , contentAlignment = Alignment.Center
            ) {
                Text( "Go Back" , color = Color.White )
            }
        }
         DisposableEffect( Unit ) {
             onDispose {
                 BackHandlerBackStack.detach( uniqueId )

             }
         }
    }
}

private object BackHandlerBackStack {
    private var _unique_id_ = Int.MIN_VALUE

    var activeValue by mutableStateOf( _unique_id_ )
    val stack = Stack<Int>()
    val attach : Int
    get() {
        activeValue = _unique_id_
        stack.add( _unique_id_ )
        return _unique_id_++
    }
    fun detach( uniqueId : Int ) {
        stack.remove( uniqueId )
        _unique_id_ = stack.peek()
        activeValue = _unique_id_
    }

    init {
        activeValue = _unique_id_
        stack.add( _unique_id_ )
        _unique_id_++
    }

}