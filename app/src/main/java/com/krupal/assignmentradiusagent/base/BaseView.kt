package com.krupal.assignmentradiusagent.base

interface BaseView {
    fun showProgressDialog(showProgress: Boolean)
    fun showError(message: CharSequence?)
}