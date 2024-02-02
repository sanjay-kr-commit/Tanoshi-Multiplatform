package tanoshi.multiplatform.common.extension.helper

import com.squareup.okhttp.Request
import com.squareup.okhttp.Request.Builder

val String.request : Request
    get() = Request.Builder()
        .url( this )
        .build()

infix fun String.request( builderScope : Builder.() -> Unit ): Request = Request.Builder()
        .url( this )
        .apply(builderScope)
        .build()

val defaultArg : Builder.() -> Unit = {
    addHeader( "User-Agent" , "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36" )
}