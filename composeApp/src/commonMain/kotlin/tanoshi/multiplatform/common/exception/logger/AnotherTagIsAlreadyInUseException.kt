package tanoshi.multiplatform.common.exception.logger

class AnotherTagIsAlreadyInUseException( tag : String) : Exception(
    "\nOnly one tag can be specified for a log scope block\nFirst One is Considered\nCurrently \"$tag\" is in active"
)