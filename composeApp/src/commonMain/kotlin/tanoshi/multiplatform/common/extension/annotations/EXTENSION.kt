package tanoshi.multiplatform.common.extension.annotations

@Target( AnnotationTarget.CLASS )
@Retention( AnnotationRetention.RUNTIME )

// marked classes will be loaded at runtime
annotation class EXTENSION(
    val type : EXTENSIONTYPE
){
    enum class EXTENSIONTYPE {
        WATCHABLE ,
        VIEWABLE ,
        READABLE
    }
}