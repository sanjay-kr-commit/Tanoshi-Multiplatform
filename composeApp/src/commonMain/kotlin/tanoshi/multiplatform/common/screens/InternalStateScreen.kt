package tanoshi.multiplatform.common.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import tanoshi.multiplatform.common.extension.annotations.IconName
import tanoshi.multiplatform.shared.SharedApplicationData

@Composable
fun InternalStateScreen( sharedAppData : SharedApplicationData ): Unit = sharedAppData.run {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding( 10.dp ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        item {
            Text( "Class Count : ${extensionManager.extensionLoader.loadedExtensionClasses.size}" )
        }
        item {
            Row( modifier = Modifier.fillMaxWidth() , verticalAlignment = Alignment.CenterVertically , horizontalArrangement = Arrangement.Center ) {
                Spacer( modifier = Modifier.height( 2.dp ).weight( 5f ).fillMaxWidth().background( Color.Black ) )
                Text( " Class Information " )
                Spacer( modifier = Modifier.height( 2.dp ).weight( 5f ).fillMaxWidth().background( Color.Black ) )
            }
        }
        extensionManager.extensionLoader.loadedExtensionClasses.forEach { ( packageAndArchiveName , extension ) ->
            item {
                Column ( modifier = Modifier.fillMaxWidth().wrapContentHeight().padding( 10.dp ) , horizontalAlignment = Alignment.Start ) {
                    Text( "Package Name : ${packageAndArchiveName.first}" )
                    Text( "Name : ${extension.name}" )
                    Text( "Icon : ${
                        extension::class.java.annotations.filterIsInstance<IconName>().firstOrNull()?.icon ?: "Icon Name Annotation Not Found"
                    }" )
                    Text( "Language : ${extension.language}" )
                    Text( "Domain List : ${extension.domainsList}" )
                    Text( "Archive Name : ${packageAndArchiveName.second}" )
                }
            }
        }
    }
}