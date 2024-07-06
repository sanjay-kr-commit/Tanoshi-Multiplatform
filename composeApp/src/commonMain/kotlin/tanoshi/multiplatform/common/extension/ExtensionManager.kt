package tanoshi.multiplatform.common.extension

import tanoshi.multiplatform.shared.extension.ExtensionManager

fun ExtensionManager.reloadExtension() {
    unloadExtensions()
    loadExtensions()
}