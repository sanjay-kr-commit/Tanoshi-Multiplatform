package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.util.logger.Logger

@Composable
fun LogScreen(
    logger : Logger
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
        for ( ( tag , message ) in logger.list ) {
            item {
                Column {
                    Text(
                        text = tag ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip( RoundedCornerShape( 5.dp ) )
                            .background( tag.color )
                            .padding( horizontal = 10.dp ) ,
                        color = Color.Black ,
                    )
                    Text(
                        text = message ,
                        color = Color.Black
                    )
                    Spacer( Modifier.height( 10.dp ) )
                }
            }
        }
//        item {
//            Text(
//                text = log ,
//                modifier = Modifier
//                    .fillMaxSize() ,
//                color = Color.Black
//            )
//        }
    }
    
}

private val String.color : Color
    get() = when ( this ) {
        "ERROR" -> Color.Red
        "WARNING" -> Color.Yellow
        "DEBUG" -> Color.Green
        else -> Color.Black
    }