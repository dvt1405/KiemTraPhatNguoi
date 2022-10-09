package roxwin.tun.baseui.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import roxwin.tun.baseui.R

class ProgressDialog(private val context: Context, cancelable: Boolean, _progressColor: Int = Color.WHITE) {
    private var progress: AlertDialog
    private val view by lazy { LayoutInflater.from(context).inflate(R.layout.dialog_progress, null, false) }

    init {
        progress = AlertDialog.Builder(context, R.style.Material_Alert_ThemeDefault_Progress)
            .setView(view)
            .setCancelable(cancelable)
            .create()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (view as ProgressBar).indeterminateTintList = ColorStateList.valueOf(_progressColor)
        }
    }


    fun show() {
        progress.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progress.show()
    }

    fun dismiss() {
        progress.dismiss()
    }
}