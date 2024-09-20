package tanoshi.multiplatform.common.util

import tanoshi.multiplatform.common.util.logger.Logger
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar

val currentDateTime : String
    get() = Calendar.getInstance().time.let { date ->
        SimpleDateFormat( "yyyy-MM-dd-HH-mm-ss" ).let { formatter ->
            formatter.format( date )
        }
    }

val String.toFile : File
    get() = File( this )

fun File.child( childFile : String ) : File = File( this , childFile )

fun logger() : Logger = Logger()

fun <T> selectableMenu(
    initialValue : T , vararg entries : T
) : SelectableMenu<T> = SelectableMenu<T>(
    initialValue , *entries
)

val String.hash256 : String
    get() = MessageDigest.getInstance("SHA-256")?.let { messageDigest ->
        messageDigest.digest( toByteArray( StandardCharsets.UTF_8 ) )?.let { hash : ByteArray? ->
            Base64.getEncoder().encodeToString( hash )
        } ?: throw Exception()
    } ?: throw Exception()