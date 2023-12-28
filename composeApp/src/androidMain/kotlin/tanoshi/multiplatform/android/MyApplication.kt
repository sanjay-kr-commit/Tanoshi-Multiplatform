package tanoshi.multiplatform.android

import android.app.Application
import android.util.Log
import android.widget.Toast

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleUncaughtException( thread , throwable )
        }
    }
    
    private fun handleUncaughtException( thread : Thread , throwable : Throwable ) {
        Log.d( "Uncaught_Exception" , throwable.stackTraceToString() )
    }
    
}