package tanoshi.multiplatform.common.util

import java.io.File

val restrictedClasses : HashSet<String> = hashSetOf(
    File::class.java.name
)