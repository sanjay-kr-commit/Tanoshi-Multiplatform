package tanoshi.multiplatform.common.util.logger

import tanoshi.multiplatform.common.exception.logger.LogFileAlreadyExist
import java.io.File
import java.lang.Exception

class Logger {
    
    private val buffer : StringBuffer = StringBuffer()
    
    fun saveLog( file : File , overwrite : Boolean = false ) {
        if ( buffer.isEmpty() || buffer.isBlank() ) return
        try {
            if ( file.isFile && !overwrite ) throw LogFileAlreadyExist( file.name )
            file.outputStream().bufferedWriter().use { logFile ->
                logFile.write( buffer.toString() )
            }
        } catch ( _ : Exception ) {}
    }
    
    val read : String
        get() = buffer.toString()
    
    infix fun log(logScope : LogScope.() -> String ) = LogScope().run {
        val message = logScope()
        val tag = toString()
        buffer.append(
            "$tag : $message\n"
        )
    }
    
    
}