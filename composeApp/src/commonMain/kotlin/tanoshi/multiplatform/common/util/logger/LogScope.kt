package tanoshi.multiplatform.common.util.logger

import tanoshi.multiplatform.common.exception.logger.AnotherTagIsAlreadyInUseException

class LogScope {

    var tag : Tag = Tag.NOTSPECIFIED
    var title : String = "UNSPECIFIED"

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

    enum class Tag( name : String ) {
        WARN( "WARNING" ) ,
        ERROR( "ERROR" ) ,
        DEBUG( "DEBUG" ) ,
        NOTSPECIFIED( "NOTSPECIFIED" ) ;

        override fun toString(): String = name

    }

}