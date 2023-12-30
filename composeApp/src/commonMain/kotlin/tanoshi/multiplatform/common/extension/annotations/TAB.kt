package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.FUNCTION )
@Retention( AnnotationRetention.RUNTIME )

// marked funtion will be loaded at runtime
annotation class TAB(
    val fieldName : String
)