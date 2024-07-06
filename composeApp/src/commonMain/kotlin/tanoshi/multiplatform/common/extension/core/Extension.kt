package tanoshi.multiplatform.common.extension.core

import tanoshi.multiplatform.common.util.SelectableMenu

interface Extension {
    val name : String

    val domainsList : SelectableMenu<String>

    val language : String

}

internal fun Extension.insertSharedDependencies(
    injectionLambda : SharedDependencies.() -> Unit
) {
    if (this !is SharedDependencies) return
    injectionLambda.invoke(this)
}