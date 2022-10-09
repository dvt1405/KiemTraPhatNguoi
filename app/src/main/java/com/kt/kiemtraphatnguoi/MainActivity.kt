package com.kt.kiemtraphatnguoi

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.kt.kiemtraphatnguoi.databinding.ActivityMainBinding
import com.kt.kiemtraphatnguoi.model.DataState
import com.kt.kiemtraphatnguoi.model.ViolationResult
import com.kt.kiemtraphatnguoi.ui.MainViewModel
import com.kt.kiemtraphatnguoi.ui.MainViewModelFactory
import com.kt.kiemtraphatnguoi.usecase.CheckViolations
import roxwin.tun.baseui.base.BaseActivity
import roxwin.tun.baseui.dialog.ProgressDialog
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val progress by lazy {
        ProgressDialog(this, false)
    }

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override val layoutRes: Int
        get() = R.layout.activity_main

    override fun initDependencies() {
        App.get()
            .component
            .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun initAction(savedInstanceState: Bundle?) {
        viewModel.resultCheck.observe(this) {
            handleUiWhenCheck(it)
        }
    }

    override fun onStop() {
        super.onStop()
        progress.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun handleUiWhenCheck(dataState: DataState<ViolationResult>) {
        when (dataState) {
            is DataState.Loading -> {
                progress.show()

            }
            is DataState.Success -> {
                progress.dismiss()

            }
            is DataState.Error -> {
                progress.dismiss()
            }
        }

    }

}