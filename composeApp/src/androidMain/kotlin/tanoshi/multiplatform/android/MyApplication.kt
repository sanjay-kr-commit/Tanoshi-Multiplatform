package tanoshi.multiplatform.android

import android.widget.Toast
import tanoshi.multiplatform.shared.SharedApplicationData

class MyApplication : SharedApplicationData() {

    override fun onCreate() {
        super.onCreate()
        extensionManager.apply {
            applicationContext = this@MyApplication.applicationContext
        }
        // set uncaught exception thread
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleUncaughtException( thread , throwable )
        }
    }
    
    private fun handleUncaughtException( thread : Thread , throwable : Throwable ) {
        logger log {
            ERROR
            throwable.stackTraceToString()
        }
    }
    
}