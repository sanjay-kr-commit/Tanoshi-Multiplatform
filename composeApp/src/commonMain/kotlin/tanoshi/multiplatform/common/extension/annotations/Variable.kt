package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.FIELD )
@Retention( AnnotationRetention.RUNTIME )
annotation class Variable(
    val publicName : String ,
    val uniqueName : String
)