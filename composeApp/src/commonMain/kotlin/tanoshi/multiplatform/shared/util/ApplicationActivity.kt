package tanoshi.multiplatform.shared.util

expect open class ApplicationActivity {

    fun <applicationActivity:ApplicationActivity> changeActivity(
        applicationActivityName : Class<applicationActivity> ,
        vararg objects : Any
    )

}