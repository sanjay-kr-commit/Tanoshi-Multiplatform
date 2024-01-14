package tanoshi.multiplatform.common.util

import tanoshi.multiplatform.common.util.logger.Logger
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar

val currentDateTime : String
    get() = Calendar.getInstance().time.let { date ->
        SimpleDateFormat( "yyyy-MM-dd-HH-mm-ss" ).let { formatter ->
            formatter.format( date )
        }
    }

val String.toFile : File
    get() = File( this )

fun logger() : Logger = Logger()

fun <T> selectableMenu(
    initialValue : T , vararg entries : T
) : SelectableMenu<T> = SelectableMenu<T>(
    initialValue , *entries
)