package com.krupal.app_domain.base

interface BaseView {
    fun showProgressDialog(showProgress: Boolean)
    fun showError(message: CharSequence?)
}