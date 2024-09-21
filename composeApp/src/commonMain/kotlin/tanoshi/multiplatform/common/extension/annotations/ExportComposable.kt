package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.FUNCTION )
@Retention( AnnotationRetention.RUNTIME )

/**
 * This annotation is used to expose functions
 * with signature () -> Unit, this function is used
 * to export @Composable () -> Unit lambda to app
 * by extending your extension with SharedDependency
 * which is used to expose application access
 */
annotation class ExportComposable(
    val composableFunctionName : String
)