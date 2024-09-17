package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.FUNCTION )
@Retention( AnnotationRetention.RUNTIME )
annotation class VariableReciever(
    vararg val variableUniqueNameList : String
)