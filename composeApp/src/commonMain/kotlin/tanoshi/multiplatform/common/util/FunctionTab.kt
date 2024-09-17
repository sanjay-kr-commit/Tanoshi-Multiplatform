package tanoshi.multiplatform.common.util

import tanoshi.multiplatform.common.extension.Entry
import java.lang.reflect.Method

data class FunctionTab(
    val method: Method,
    val function : (Int)->List<Entry<*>>,
    val publicName: String,
    val variableUniqueNameList : List<String>
)