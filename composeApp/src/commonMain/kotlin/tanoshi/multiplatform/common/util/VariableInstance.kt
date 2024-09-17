package tanoshi.multiplatform.common.util

import java.lang.reflect.Field

data class VariableInstance(
    val field: Field,
    val uniqueName : String,
    val publicName : String,
)