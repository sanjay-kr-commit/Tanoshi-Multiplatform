package tanoshi.multiplatform.android

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import tanoshi.multiplatform.shared.SharedApplicationData


class MyApplication : SharedApplicationData() {

    var manageStorage : Boolean by mutableStateOf( false )

    override fun onCreate() {
        super.onCreate()
        try {
            privateDir = getDir( "tanoshi" , MODE_PRIVATE )
            logger log {
                title = "Private Storage Path"
                privateDir.absolutePath
            }
        } catch ( e : Exception ) {
            logger log {
                title = "Cannot Access App Internal Storage Path"
                e.stackTraceToString()
            }
        }
        try {
            publicDir = Environment.getExternalStorageDirectory()
            logger log {
                title = "Internal Storage Path"
                publicDir.absolutePath
            }
        } catch ( e : Exception ) {
            logger log {
                title = "Cannot Access Internal Storage Path"
                e.stackTraceToString()
            }
        }
        extensionManager.apply {
            applicationContext = this@MyApplication.applicationContext
        }
        manageStorage = checkForStoragePermission()
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

    fun checkForStoragePermission() : Boolean = when {
        SDK_INT >= Build.VERSION_CODES.R -> Environment.isExternalStorageManager()
        else -> {
            val result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
            val result1 = ContextCompat.checkSelfPermission(this , WRITE_EXTERNAL_STORAGE)
             result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    
}