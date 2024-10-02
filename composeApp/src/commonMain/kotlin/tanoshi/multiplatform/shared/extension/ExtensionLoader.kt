package tanoshi.multiplatform.shared.extension

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.extension.ExtensionPackage
import tanoshi.multiplatform.common.util.logger.Logger
import tanoshi.multiplatform.common.util.toast.ToastTimeout

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ExtensionLoader {

    var logger : Logger?

    var startDynamicActivity : ((@Composable ()->Unit).() -> Unit)?

    var setToastLambda : (String.(ToastTimeout)->Unit)?

    // pair( pair(package name , archive name) , extension )
    //val loadedExtensionClasses : ArrayList< Pair< Pair<String,String> , Extension> >
    val loadedExtensionPackage : ArrayList<ExtensionPackage>
    
    fun loadTanoshiExtension( extensionPackage: ExtensionPackage, classNameList : List<String> )

    fun reloadClass( className : String , extensionPackage: ExtensionPackage )

}