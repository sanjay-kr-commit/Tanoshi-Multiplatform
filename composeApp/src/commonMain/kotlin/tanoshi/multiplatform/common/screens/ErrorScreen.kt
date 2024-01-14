package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ErrorScreen(
    log : String
) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
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
        item {
            Text( "Hello" )
        }
    }
    
}