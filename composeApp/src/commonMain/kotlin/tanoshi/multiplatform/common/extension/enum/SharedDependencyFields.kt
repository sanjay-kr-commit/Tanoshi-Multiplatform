package tanoshi.multiplatform.common.extension.enum

enum class SharedDependencyFields( val description : String , val defaultValue : Boolean ){
    StartComposableView( "Allow Extension To Export Ui" , false ) ,
    Logger( "Share Application Logger To Extension" , true ) ,
    ShowToast( "Allow Extension To Show Toast" , true )
}