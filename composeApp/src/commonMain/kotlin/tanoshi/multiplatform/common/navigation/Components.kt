package tanoshi.multiplatform.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import kotlin.reflect.KProperty

// This Component file is made so
// That all the function that are needed to make navigation stack
// can be imported by one import statement
// i.e import tanoshi.multiplatform.shared.naviagtion.*

// All the Nav Controller Component

fun navController(
    startScreen: String ,
    stack : MutableSet<String> = mutableSetOf()
) : NavigationController = NavigationController( startScreen , stack )

@Composable
fun NavController (
    startScreen : String ,
    stack : MutableSet<String> = mutableSetOf()
) : MutableState<NavigationController> = rememberSaveable {
    mutableStateOf( NavigationController( startScreen, stack ) )
}
operator fun NavigationController.getValue( thisObj : Any? , kProperty: KProperty<*>) : NavigationController = this
infix fun NavigationController.backStackLambdaPush( lambda : BackLambdaStack.() -> Any ) = backLambdaStackObject.push(lambda)
fun NavigationController.backStackLambdPeek() = backLambdaStackObject.peek()
fun NavigationController.backStackLambdaPop() = backLambdaStackObject.pop()
val NavigationController.back : Unit
    get() {
        back()
    }

// All The Navigation Host Component
@Composable
fun CreateScreenCatalog(
    navigationController: NavigationController ,
    screen : @Composable NavigationHost.NavigationGraphBuilder.() -> Unit
) = NavigationHost( navigationController , screen ).also { it.renderView() }

@Composable
fun NavigationHost.NavigationGraphBuilder.Screen(
    route: String,
    content: @Composable () -> Unit
) {
    if (navigationController.currentScreen == route) content()
}