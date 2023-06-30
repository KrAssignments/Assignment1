package com.krupal.assignmentradiusagent.presenter

import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.krupal.assignmentradiusagent.service.FacilitiesDownloadService

class MainActivityPresenter private constructor() : MainContract.Presenter {

    private lateinit var context: Context

    private constructor(context: Context) : this() {
        this.context = context
    }

    companion object {
        private lateinit var INSTANCE: MainActivityPresenter
        fun getInstance(context: Context) {
            INSTANCE = MainActivityPresenter(context)
        }
    }

    override fun getFacilitiesFromRemote() {
        val request = OneTimeWorkRequest.Builder(FacilitiesDownloadService::class.java)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork("request", ExistingWorkPolicy.REPLACE, request)
    }

    override fun getFacilitiesFromLocal(type: String, facilityId: Int, optionId: Int) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TODO("Not yet implemented")
    }

    override fun setView(view: MainContract.View?) {
        TODO("Not yet implemented")
    }
}