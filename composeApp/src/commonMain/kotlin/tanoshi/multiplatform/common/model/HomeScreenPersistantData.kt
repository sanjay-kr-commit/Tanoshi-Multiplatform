package tanoshi.multiplatform.common.model

import tanoshi.multiplatform.common.extension.Entry
import tanoshi.multiplatform.common.extension.core.Extension

class HomeScreenPersistantData {

    lateinit var entries : MutableList<Pair<Extension<*>,Entry<*>>>

}