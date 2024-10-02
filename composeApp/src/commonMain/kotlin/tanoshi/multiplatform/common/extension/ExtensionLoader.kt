package tanoshi.multiplatform.common.extension

import androidx.compose.runtime.Composable
import tanoshi.multiplatform.common.db.Preferences.addPreference
import tanoshi.multiplatform.common.db.Preferences.withPrefix
import tanoshi.multiplatform.common.extension.core.Extension
import tanoshi.multiplatform.common.extension.core.SharedDependencies
import tanoshi.multiplatform.common.extension.enum.SharedDependencyFields
import tanoshi.multiplatform.shared.extension.ExtensionLoader

fun ExtensionLoader.loadExtensionPermission(
    className : String ,
    extension: Extension<*> ,
    extensionNameSpace : String
) {

    if ( extension !is SharedDependencies ) return

    className.withPrefix.also { preference ->

        preference[className+SharedDependencyFields.StartComposableView.name]?.let { value ->
            if ( value.toBoolean() ) extension.exportComposable = {
                startDynamicActivity?.let { (this as (@Composable () -> Unit ) ).it() }
            }
        } ?: run {
            addPreference(
                className+SharedDependencyFields.StartComposableView.name ,
                SharedDependencyFields.StartComposableView.defaultValue.toString() ,
                extensionNameSpace
            )
        }

        preference[className+SharedDependencyFields.Logger.name]?.let { value ->
            if ( value.toBoolean() ) extension.logger = logger
        } ?: run {
            addPreference(
                className+SharedDependencyFields.Logger.name ,
                SharedDependencyFields.Logger.defaultValue.toString(),
                extensionNameSpace
            )
        }

        preference[className+SharedDependencyFields.ShowToast.name]?.let { value ->
            if ( value.toBoolean() ) extension.showToast = setToastLambda
        } ?: run {
            addPreference(
                className+SharedDependencyFields.ShowToast.name ,
                SharedDependencyFields.ShowToast.defaultValue.toString() ,
                extensionNameSpace
            )
        }

    }

}