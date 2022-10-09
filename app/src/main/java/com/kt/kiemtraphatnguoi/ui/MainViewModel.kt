package com.kt.kiemtraphatnguoi.ui

import android.text.Spanned
import android.util.Log
import androidx.core.text.parseAsHtml
import androidx.lifecycle.*
import com.kt.kiemtraphatnguoi.base.network.ICheckViolationDataSource
import com.kt.kiemtraphatnguoi.model.DataState
import com.kt.kiemtraphatnguoi.model.ViolationResult
import roxwin.tun.baseui.base.BaseViewModel
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(
    private val interactors: MainInteractors
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(interactors) as T
    }
}

class MainViewModel(private val interactors: MainInteractors) : BaseViewModel() {
    val bks by lazy { MutableLiveData<String>() }
    val isValid by lazy { MediatorLiveData<Boolean>() }
    val result by lazy { MediatorLiveData<String>() }
    val isLoading by lazy { MediatorLiveData<Boolean>() }
    private val _resultCheck by lazy { MutableLiveData<DataState<ViolationResult>>() }
    val resultCheck: LiveData<DataState<ViolationResult>>
        get() = _resultCheck

    init {
        isValid.addSource(bks) {
            isValid.postValue(it.isValid())
        }
        isLoading.addSource(_resultCheck) {
            isLoading.postValue(it is DataState.Loading)
        }
        result.addSource(_resultCheck) {
            handleResult(it)
        }

    }

    fun onClick() {
        _resultCheck.postValue(DataState.Loading())
        addDisposable(
            interactors.checkViolations(bks.value!!, ICheckViolationDataSource.Type.KiemTraPhatNguoi)
                .subscribe({
                    _resultCheck.postValue(DataState.Success(it))
                }, {
                    _resultCheck.postValue(DataState.Error(it))
                })
        )
    }

    private fun handleResult(violationRs: DataState<ViolationResult>) {
        if (violationRs !is DataState.Success) return
        when (violationRs.data) {
            is ViolationResult.FromPhatNguoiXe -> {
                result.postValue(violationRs.data.htmlText.parseAsHtml().toString())
            }
            is ViolationResult.FromKGO -> {
                result.postValue(violationRs.data.response.toString())
            }

            else -> {

            }
        }
    }

    fun String.isValid() = this.matches(Regex("(\\d{2}\\w{1}\\d{5})|(\\d{2}\\w{1}\\d{4})"))
}