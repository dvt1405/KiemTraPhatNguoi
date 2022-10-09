package roxwin.tun.baseui.utils

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.*


object LogUtil {
    private val mFileName: String? = null
    val DEFAULT_WORK_SPACE = Environment.getExternalStorageDirectory().absolutePath + "/acar/"
    const val LOG_FILE_NAME = "log.txt"
    const val TAG = "A-Car Log"
    const val EXCEPTION_TAG = "Error"

    @JvmStatic
    fun log(message: String?) {
        try {
            val sdcardDstDir = DEFAULT_WORK_SPACE
            val dir = File(sdcardDstDir)
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.i(TAG, "mkdir failed: $sdcardDstDir")
                } else {
                    Log.i(TAG, "mkdir ok: $sdcardDstDir")
                    log(message)
                }
            } else {
                Log.w(TAG, "$sdcardDstDir already exists! ")
                val file = File(sdcardDstDir, LOG_FILE_NAME)
                val fos = FileOutputStream(file, true)
                val myOutWriter = OutputStreamWriter(fos)
                val time = StringBuilder()
                val calendar = Calendar.getInstance(Locale.getDefault())
                time.append(calendar[Calendar.DAY_OF_MONTH])
                    .append("-")
                    .append(calendar[Calendar.MONTH]+1)
                    .append("-")
                    .append(calendar[Calendar.YEAR])
                    .append(":")
                myOutWriter.append(time.toString())
                    .append(message).append("\n")
                myOutWriter.close()
                fos.close()
            }
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }

    @JvmStatic
    fun log(tag: String, message: String) {
        log("$tag:\t$message")
    }

    fun logNormal(tag: String?, message: String?, vararg throwable: Exception) {
        for (mThrowable in throwable) {
            Log.e(tag, mThrowable.message, mThrowable)
        }
    }

    fun logNormal(ex: Exception?) {
        logNormal(TAG, "", ex!!)
    }

    @JvmStatic
    fun log(ex: Exception) {
        ex.printStackTrace()
        val message = StringBuilder()
        message.append(ex.message).append("\n")
        val stackTraceElement = ex.stackTrace
        for (traceElement in stackTraceElement) {
            message.append(traceElement.fileName)
                .append(" ")
                .append(traceElement.className)
                .append(" ")
                .append(traceElement.methodName)
                .append(" ")
                .append(traceElement.lineNumber)
                .append("\n")
        }
        log(message.toString())
    }

    @JvmStatic
    fun log(ex: Throwable) {
        val message = StringBuilder()
        message.append(ex.message).append("\n")
        val stackTraceElement = ex.stackTrace
        for (traceElement in stackTraceElement) {
            message.append(traceElement.fileName)
                .append(" ")
                .append(traceElement.className)
                .append(" ")
                .append(traceElement.methodName)
                .append(" ")
                .append(traceElement.lineNumber)
                .append("\n")
        }
        log(message.toString())
    }

    @JvmStatic
    fun logLongMessage(message: String) {
        val maxLogSize = 1000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = Math.min(end, message.length)
            Log.e(TAG, message.substring(start, end))
        }
    }

}
