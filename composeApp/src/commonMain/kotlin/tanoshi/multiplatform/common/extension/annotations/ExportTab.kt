package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.FUNCTION )
@Retention( AnnotationRetention.RUNTIME )

/**
 * Exposes annotated function to application
 * Annotated function will only be registered
 * if it's signature is
 * ( int ) -> List<Entry<?>>
 */
annotation class ExportTab(
    val tabName : String
)