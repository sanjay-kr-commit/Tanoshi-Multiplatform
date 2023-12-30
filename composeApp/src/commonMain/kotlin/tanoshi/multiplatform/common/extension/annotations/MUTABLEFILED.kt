package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.FIELD )
@Retention( AnnotationRetention.RUNTIME )
// this is annoation is used to look for changing field
// present in an extension class
// acts as customization button for that class
annotation class MUTABLEFILED(
    val fieldName : String
)