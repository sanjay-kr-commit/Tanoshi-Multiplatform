package tanoshi.multiplatform.common.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.util.logger.Logger

@Composable
fun LogScreen(
    logger : Logger
) {

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding( 10.dp )
            .clip( RoundedCornerShape( 10.dp ) )
            .background( MaterialTheme.colorScheme.onPrimaryContainer.copy( 0.2f ) )
            .padding( 10.dp )
            .clip( RoundedCornerShape( 10.dp ) )
    ){

        logger.list.forEach { ( heading , stackTrace ) ->
            var isExpanded by   mutableStateOf( false )
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip( RoundedCornerShape( 10.dp ) )
                        .background( MaterialTheme.colorScheme.onSecondary.copy( 0.5f ))
                        .padding(
                            if ( isExpanded ) 5.dp else 0.dp
                        )
                        .animateContentSize()
                        .clip( RoundedCornerShape( 10.dp ) )
                        .clickable {
                            isExpanded = !isExpanded
                        }
                ) {
                    Column ( modifier = Modifier.fillMaxWidth().clip( RoundedCornerShape( 10.dp ) ).background( heading.first.colorCode ) ) {
                        Spacer( Modifier.height( 10.dp ) )
                        Text( heading.second , modifier = Modifier.padding( start = 5.dp ) , color = Color.White )
                        Spacer( Modifier.height( 10.dp ) )
                    }

                    if ( isExpanded ) {
                        Spacer( Modifier.height( 2.dp ) )
                        Text( stackTrace , color = MaterialTheme.colorScheme.onSecondaryContainer )
                    }
                }
            }
            item { Spacer( modifier = Modifier.height( 10.dp ) ) }
        }
    }
    
}

private val String.colorCode : Color
    get() = when ( this ) {
        "ERROR" -> Color( 139 , 0 , 0 )
        "WARNING" -> Color( 245 , 140 , 0)
        "DEBUG" -> Color( 1 , 50 , 32 )
        else -> Color.Black
    }