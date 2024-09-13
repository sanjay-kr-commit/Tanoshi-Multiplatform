package tanoshi.multiplatform.android

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import tanoshi.multiplatform.android.activities.CrashHandlingActivity
import tanoshi.multiplatform.shared.SharedApplicationData

class MyApplication : SharedApplicationData() {

    var manageStorage : Boolean by mutableStateOf( false )

    override fun onCreate() {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleUncaughtException( thread , throwable )
        }
        super.onCreate()
    }
    
    private fun handleUncaughtException( thread : Thread , throwable : Throwable ) {
        logger log {
            ERROR
            title = "Uncaught Exception"
            throwable.stackTraceToString()
        }
        Log.e( "err" , throwable.stackTraceToString() )
        Toast.makeText( this , "error" , Toast.LENGTH_SHORT ).show()
        startActivity( Intent( this , CrashHandlingActivity::class.java ).apply {
            flags = Intent.FLAG_FROM_BACKGROUND
            flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    fun checkForStoragePermission() : Boolean = when {
        SDK_INT >= Build.VERSION_CODES.R -> Environment.isExternalStorageManager()
        else -> {
            val result = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
            val result1 = ContextCompat.checkSelfPermission(this , WRITE_EXTERNAL_STORAGE)
             result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

//    private fun receiveCallback(){
//        if (Build.VERSION.SDK_INT >= 34) {
//            ZipPathValidator.setCallback(object : ZipPathValidator.Callback {
//                override fun onZipEntryAccess(path: String) {
//                    Log.d( "invalid" , path )
//                }
//            })
//        }
//    }

    
}