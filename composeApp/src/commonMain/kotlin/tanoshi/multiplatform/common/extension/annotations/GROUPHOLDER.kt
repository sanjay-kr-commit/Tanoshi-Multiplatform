package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.FUNCTION )
@Retention( AnnotationRetention.RUNTIME )

// This annotation is used to filter out various filed that are not relevant to function 
// so those which are relevent can be changed at runtime before function is invoked
// these are grouped using groupId
annotation class GROUPHOLDER(
    vararg val groupId : String
)