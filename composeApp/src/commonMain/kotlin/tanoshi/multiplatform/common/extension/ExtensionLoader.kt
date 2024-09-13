package tanoshi.multiplatform.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.core.SharedDependencies
import tanoshi.multiplatform.common.extension.enum.SharedDependencyFields
import tanoshi.multiplatform.shared.extension.ExtensionLoader
import java.io.File
import java.util.LinkedList

fun ExtensionLoader.loadExtensionPermission(
    className : String ,
    extension: Extension , file : File
) {
    if ( extension !is SharedDependencies ) return
    if ( !file.isFile ) {
        createExtensionPermissionFile( extension , file )
        return
    }
    val buffer = StringBuffer()
    file.bufferedReader().use { bufferedReader ->
        // read shared dependency permission
        repeat( SharedDependencyFields.entries.size ) {
            val pair = bufferedReader.readLine().split( ":" )
            pair.firstOrNull()?.let { buffer.append( it ) }
            if ( pair.size > 1 && pair[1] == "${true}" ) {
                buffer.append( " : True\n" )
                when ( SharedDependencyFields.valueOf( pair.first() ) ) {
                    SharedDependencyFields.StartComposableView -> extension.startComposableView = {
                        startDynamicActivity?.let { (this as (@Composable () -> Unit ) ).it() }
                    }
                    SharedDependencyFields.Logger -> extension.logger = logger
                    SharedDependencyFields.ShowToast -> extension.showToast = setToastLambda
                }
            } else buffer.append( " : False\n" )
        }


    }
    logger?. log {
        DEBUG
        title = "Permission : $className"
        buffer.toString()
    }
}

fun loadExtensionPermissionSettingPage(
    className : String ,
    extension: Extension , file : File
) {
    val entries : ArrayList<Pair<SharedDependencyFields,MutableState<Boolean>>> = arrayListOf()


}