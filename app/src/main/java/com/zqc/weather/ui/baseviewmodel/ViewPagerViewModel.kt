package com.zqc.weather.ui.baseviewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewPagerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val homePageSelectedIndex = "home_page_selected_index"

    private val mSelectState = mutableStateOf(0)

    fun getSelectIndex(): State<Int> {
        val index = savedStateHandle.get<Int>(homePageSelectedIndex) ?: 0
        mSelectState.value = index
        return mSelectState
    }

    fun saveSelectIndex(selectIndex: Int) {
        savedStateHandle[homePageSelectedIndex] = selectIndex
        mSelectState.value = selectIndex
    }

    private fun removeIndex() {
        savedStateHandle.remove<Int>(homePageSelectedIndex)
    }

    override fun onCleared() {
        super.onCleared()
//        removeIndex()
        Log.i("${ViewPagerViewModel::class.simpleName}", "onCleared invoke")
    }
}