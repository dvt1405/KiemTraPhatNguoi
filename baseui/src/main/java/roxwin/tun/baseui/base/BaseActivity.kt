package roxwin.tun.baseui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.InstallStatus
import roxwin.tun.baseui.R

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    protected val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    abstract val layoutRes: Int
    lateinit var binding: T
    var topFragmentTag: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        initDependencies()
        initView(savedInstanceState)
        initAction(savedInstanceState)
    }

    abstract fun initDependencies()
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initAction(savedInstanceState: Bundle?)

    fun addFragment(fragment: Fragment, customAnimation: Boolean = true, container: Int = R.id.container) {
        topFragmentTag = fragment::class.java.name
        supportFragmentManager.beginTransaction()
            .also {
                if (customAnimation) {
                    it.setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_right,
                        R.anim.enter_from_right,
                        R.anim.exit_to_right
                    )
                }
            }
            .add(container, fragment, topFragmentTag)
            .addToBackStack(topFragmentTag)
            .commit()
    }

    fun replaceFragment(fragment: Fragment, customAnimation: Boolean = true, container: Int = R.id.container) {
        topFragmentTag = fragment::class.java.name
        supportFragmentManager.beginTransaction()
            .also {
                if (customAnimation) {
                    it.setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_right
                    )
                }
            }
            .replace(container, fragment, topFragmentTag)
            .addToBackStack(topFragmentTag)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v: View? = currentFocus
        if (v != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && v is EditText &&
            !v::class.java.name.startsWith("android.webkit.")
        ) {
            val sourceCoordinates = IntArray(2)
            v.getLocationOnScreen(sourceCoordinates)
            val x: Float = ev.rawX + v.getLeft() - sourceCoordinates[0]
            val y: Float = ev.rawY + v.getTop() - sourceCoordinates[1]
            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                hideKeyboard(this)
                v.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
    }

    protected fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            binding.root,
            getString(R.string.update_app_downloaded_title),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            setActionTextColor(resources.getColor(R.color.bg_grey_80a))
            show()
        }
    }

    override fun onDestroy() {
        hideKeyboard(this)
        super.onDestroy()
    }

    fun hideKeyboard(activity: Activity?) {
        if (activity?.window != null) {
            activity.window.decorView
            val imm: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}