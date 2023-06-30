package com.krupal.app_domain.presenter

import android.app.Application
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.krupal.app_data.database.AppDb
import com.krupal.app_data.database.entity.ExclusionTable
import com.krupal.app_data.database.entity.FacilityTable
import com.krupal.app_domain.entity.ExclusionEntity
import com.krupal.app_domain.entity.FacilityEntity
import com.krupal.app_domain.service.FacilitiesDownloadService
import io.realm.Realm
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class FacilitiesPresenter(private val application: Application) : FacilitiesContract.Presenter {
    init {
        Realm.init(application.applicationContext)
    }

    private val tag: String = this.javaClass.simpleName
    private var view: FacilitiesContract.View? = null
    private val job: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val jobList: MutableList<Job> = mutableListOf()

    override fun getFacilitiesFromRemote(forceUpdate: Boolean) {
        val request =
            PeriodicWorkRequest.Builder(FacilitiesDownloadService::class.java, 1, TimeUnit.DAYS)
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                ).build()
        WorkManager.getInstance(application.applicationContext).enqueueUniquePeriodicWork(
            "request", if (forceUpdate) ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE
            else ExistingPeriodicWorkPolicy.KEEP, request
        )
    }

    override fun getFacilitiesFromLocal(type: String, facilityId: Int?, optionId: Int?) {
        Log.d(tag, "called getFacilitiesFromLocal")

        jobList.add(job.launch {
            withContext(Dispatchers.Main) {
                view?.showProgressDialog(true)
            }
            val list = mutableListOf<FacilityEntity>()
            val list2 = mutableListOf<ExclusionEntity>()
            val db: Realm = Realm.getInstance(AppDb.appDBConfig)
            db.beginTransaction()
            list2.addAll(db.where(ExclusionTable::class.java).findAll().toList().map {
                ExclusionEntity(
                    id = it.id.timestamp,
                    facilityId = it.facilityId,
                    optionId = it.optionId,
                    exclusionFacilityId = it.exclusionFacilityId,
                    exclusionOptionId = it.exclusionOptionId,
                )
            })

            db.where(FacilityTable::class.java).findAll().toList().forEach { item ->
                list.add(
                    FacilityEntity(
                        id = item.id.timestamp,
                        facilityId = item.facilityId,
                        facilityName = item.facilityName,
                        optionId = item.optionId,
                        optionName = item.optionName,
                        optionIconName = item.optionIconName,
                    )
                )
            }
            db.commitTransaction()
            db.close()
            withContext(Dispatchers.Main) {
                view?.showProgressDialog(false)
                view?.onFacilitiesLoaded(list, list2)
            }
        })
    }

    override fun onDestroy() {
        jobList.forEach { cj ->
            if (cj.isActive) cj.cancel("FacilityPresenter ondestroy called")
        }
        if (job.isActive) {
            job.cancel(CancellationException("FacilityPresenter ondestroy called"))
        }
    }

    override fun setView(view: FacilitiesContract.View) {
        this.view = view
    }
}