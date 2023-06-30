package com.krupal.app_domain.base

import android.content.Intent

interface BasePresenter<T> {
    fun onDestroy()
    fun setView(view: T)
}