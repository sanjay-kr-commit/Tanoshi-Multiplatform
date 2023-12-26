package tanoshi.multiplatform.shared.naviagtion

import androidx.compose.runtime.Composable

class NavigationHost(
    val navigationController: NavigationController ,
    val content : @Composable NavigationGraphBuilder.() -> Unit
) {

    @Composable
    fun renderView() {
        NavigationGraphBuilder().renderScreen()
    }

    inner class NavigationGraphBuilder(
        val navigationController: NavigationController = this@NavigationHost.navigationController
    ) {
        @Composable
        fun renderScreen() {
            this@NavigationHost.content(this)
        }

    }


}