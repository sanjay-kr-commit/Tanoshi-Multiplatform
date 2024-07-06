package tanoshi.multiplatform.android

import tanoshi.multiplatform.shared.SharedApplicationData

class MyApplication : SharedApplicationData() {

    var manageStorage : Boolean = false

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
            title = "Uncaught Exception"
            throwable.stackTraceToString()
        }
        startCrashActivity()
    }

    
}