package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.CLASS )
@Retention( AnnotationRetention.RUNTIME )
annotation class IconName(
    val icon : String = ""
)