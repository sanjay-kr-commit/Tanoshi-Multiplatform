package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.FIELD )
@Retention( AnnotationRetention.RUNTIME )

// filed marked with this annotation are loaded at runtime
// to make changes to them
// so when a function is invoked they can use these modified fields
// these are grouped using groupId
annotation class GROUPMEMBER(
    vararg val groupId : String
)