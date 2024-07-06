package tanoshi.multiplatform.common.util

import java.io.File

val restrictedClasses : Set<String> = hashSetOf(
    File::class.java.name
)