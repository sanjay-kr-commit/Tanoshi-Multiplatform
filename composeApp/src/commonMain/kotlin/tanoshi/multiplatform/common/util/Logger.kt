package tanoshi.multiplatform.common.util

import tanoshi.multiplatform.common.exception.AnotherTagIsAlreadyInUseException
import tanoshi.multiplatform.common.exception.LogFileAlreadyExist
import java.io.File
import java.lang.Exception

class Logger {
    
    private val buffer : StringBuffer = StringBuffer()
    
    fun saveLog( file : File , overwrite : Boolean = false ) {
        try {
            if ( file.isFile && !overwrite ) throw LogFileAlreadyExist( file.name )
            file.outputStream().bufferedWriter().use { logFile ->
                logFile.write( buffer.toString() )
            }
        } catch ( _ : Exception ) {}
    }
    
    val read : String
        get() = buffer.toString()
    
    infix fun write( logScope : LogScope.() -> String ) = LogScope().run {
        val message = logScope()
        val tag = toString()
        buffer.append(
            "$tag : $message\n"
        )
    }
    
    inner class LogScope {
        
        private var tag : Tag = Tag.NOTSPECIFIED
        val WARN : Unit
            get() {
                if ( tag != Tag.NOTSPECIFIED ) throw AnotherTagIsAlreadyInUseException( tag.toString() )
                tag = Tag.WARN
            }
        val ERROR : Unit
            get() {
                if ( tag != Tag.NOTSPECIFIED ) throw AnotherTagIsAlreadyInUseException( tag.toString() )
                tag = Tag.ERROR
            }
        val DEBUG : Unit
            get() {
                if ( tag != Tag.NOTSPECIFIED ) throw AnotherTagIsAlreadyInUseException( tag.toString() )
                tag = Tag.DEBUG
            }

        override fun toString(): String = tag.toString()
        
    }
    
    private enum class Tag( name : String ) {
        WARN( "WARNING" ) ,
        ERROR( "ERROR" ) ,
        DEBUG( "DEBUG" ) ,
        NOTSPECIFIED( "NOTSPECIFIED" ) ;

        override fun toString(): String = name
        
    }
    
}