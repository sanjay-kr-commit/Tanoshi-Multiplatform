package tanoshi.multiplatform.shared.naviagtion

import androidx.compose.runtime.*

// keep the current screen information and backstack
class NavigationController(
    private val startScreen : String ,
    stack : MutableSet<String> = mutableSetOf()
) {
    var currentScreen by mutableStateOf( startScreen )
    private var screenBackstack = stack
    
    infix fun navigateTo( nextScreen : String ) {
        if ( currentScreen == nextScreen ) return
        if ( screenBackstack.contains( currentScreen ) ) screenBackstack.remove( currentScreen )
        if ( nextScreen == startScreen ) screenBackstack = mutableSetOf()
        else screenBackstack.add( currentScreen )
        currentScreen = nextScreen
    }
    
    // back lambda stack it contains all that task that need to be done before invoking
    // screen back function
    val backLambdaStackObject = BackLambdaStack.backLambdaStack()
    
    // back function
    fun back() {
        backLambdaStackObject.peek()?.run {
            if ( invoke(backLambdaStackObject) != backLambdaStackObject.keepAfterInvocation ) backLambdaStackObject.pop()
            return@back
        }
        if ( screenBackstack.isEmpty() ) return
        currentScreen = screenBackstack.last()
        screenBackstack.remove( currentScreen )
    }
        
}