package tanoshi.multiplatform.common.exception

 class SelectableMenuEntryNotFoundException( entry : String ) : Exception(
     "Entry \"$entry\" Not Found In List"
 )