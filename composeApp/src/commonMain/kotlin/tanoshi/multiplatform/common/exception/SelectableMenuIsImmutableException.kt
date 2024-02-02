package tanoshi.multiplatform.common.exception

import tanoshi.multiplatform.common.util.SelectableMenu

class SelectableMenuIsImmutableException( selectableMenuObj : SelectableMenu<*> ) : Exception(
     "SelectableMenu object ${selectableMenuObj.hashCode()} is immutable"
 )