package tanoshi.multiplatform.common.util.logger

import tanoshi.multiplatform.common.exception.logger.LogFileAlreadyExist
import java.io.File
import java.lang.Exception

class Logger {
    
    private val log : ArrayList<Pair<Pair<String,String>,String>> = ArrayList()
    
    fun saveLog( file : File , overwrite : Boolean = false ) {
        if ( log.isEmpty() ) return
        try {
            if ( file.isFile && !overwrite ) throw LogFileAlreadyExist( file.name )
            file.outputStream().bufferedWriter().use { logFile ->
                logFile.write( toString() )
            }
        } catch ( _ : Exception ) {}
    }
    
    val read : String
        get() = toString()

    val list : List<Pair<Pair<String,String>,String>>
        get() = log

    infix fun log(logBlock : LogScope.() -> String ) = LogScope().run {
        val message = logBlock()
        log.add(
            Pair( Pair(tag.name,title) , message )
        )
    }

    override fun toString(): String {
        val buffer = StringBuilder()
        for ( ( tag , message ) in log ) buffer.append( "$tag : $message" )
        return buffer.toString()
    }


}