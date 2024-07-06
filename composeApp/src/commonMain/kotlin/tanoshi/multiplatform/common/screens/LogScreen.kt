package tanoshi.multiplatform.common.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.util.logger.Logger

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LogScreen(
    logger : Logger
) {

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .background( Color.White )
            .padding( 10.dp )
            .clip( RoundedCornerShape( 10.dp ) )
            .background( Color.LightGray )
            .padding( 10.dp )
            .clip( RoundedCornerShape( 5.dp ) )
    ){

        logger.list.forEach { ( heading , stackTrace ) ->
            var isExpanded by   mutableStateOf( false )
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip( RoundedCornerShape( 10.dp ) )
                        .background( Color.White )
                        .padding(
                            if ( isExpanded ) 5.dp else 0.dp
                        )
                        .animateContentSize()
                        .onClick { isExpanded = !isExpanded }
                ) {
                    Column ( modifier = Modifier.fillMaxWidth().clip( RoundedCornerShape( 10.dp ) ).background( heading.first.color ) ) {
                        Spacer( Modifier.height( 10.dp ) )
                        Text( heading.second , color = Color.White , modifier = Modifier.padding( start = 5.dp ) )
                        Spacer( Modifier.height( 10.dp ) )
                    }

                    if ( isExpanded ) {
                        Spacer( Modifier.height( 2.dp ) )
                        Text( stackTrace , color = Color.Black  )
                    }
                }
            }
            item { Spacer( modifier = Modifier.height( 10.dp ) ) }
        }
    }
    
}

private val String.color : Color
    get() = when ( this ) {
        "ERROR" -> Color( 139 , 0 , 0 )
        "WARNING" -> Color( 245 , 140 , 0)
        "DEBUG" -> Color( 1 , 50 , 32 )
        else -> Color.Black
    }