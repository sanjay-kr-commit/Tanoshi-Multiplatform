package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LogScreen(
    log : String
) {
//    val scrollState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()
    LazyColumn (
        modifier = Modifier
//            .draggable(
//                orientation = Orientation.Horizontal,
//                state = rememberDraggableState { delta ->
//                    coroutineScope.launch {
//                        scrollState.scrollBy(-delta)
//                    }},
//                )
            .fillMaxSize()
            .background( Color.White )
            .padding( 10.dp )
            .clip( RoundedCornerShape( 10.dp ) )
            .background( Color.LightGray )
            .padding( 10.dp )
    ){
        item { 
            Text(
                text = log ,
                modifier = Modifier
                    .fillMaxSize() ,
                color = Color.Black
            )
        }
    }
    
}