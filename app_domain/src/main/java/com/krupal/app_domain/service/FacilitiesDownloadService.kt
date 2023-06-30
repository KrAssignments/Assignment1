package com.krupal.app_domain.service

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.krupal.app_data.database.entity.ExclusionTable
import com.krupal.app_data.database.entity.FacilityTable
import com.krupal.app_data.network.ApiService
import com.krupal.app_data.network.RetrofitClient
import com.krupal.app_data.network.entity.APIResponse
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import org.json.JSONObject

class FacilitiesDownloadService constructor(context: Context, params: WorkerParameters) :
    ListenableWorker(context, params) {

    init {
        Realm.init(context.applicationContext)
    }
    private val TAG: String = FacilitiesDownloadService::class.java.simpleName
    private val apiService: ApiService = RetrofitClient.client.create(ApiService::class.java)
    private var disposable: Disposable? = null
    private val job = CoroutineScope(Dispatchers.IO)

    private val callback = CallbackToFutureAdapter.Resolver { callbackResult ->
        apiService.getData().subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
            .subscribe(object : SingleObserver<APIResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onSuccess(response: APIResponse) {
                    try {
                        job.launch {
                            val db: Realm = Realm.getDefaultInstance()
                            db.beginTransaction()
                            db.delete(ExclusionTable::class.java)
                            db.delete(FacilityTable::class.java)
                            response.exclusions.forEach { exclusion ->
                                db.createObjectFromJson(
                                        ExclusionTable::class.java,
                                        JSONObject().apply {
                                            this.put("id", ObjectId())
                                            this.put("facilityId", exclusion[0].facilityID.toInt())
                                            this.put("optionId", exclusion[0].optionsID.toInt())
                                            this.put(
                                                "exclusionFacilityId",
                                                exclusion[1].facilityID.toInt()
                                            )
                                            this.put(
                                                "exclusionOptionId",
                                                exclusion[1].optionsID.toInt()
                                            )
                                        })

                            }
                            response.facilities.forEach { facility ->
                                facility.options.forEach { option ->
                                    db.createObjectFromJson(
                                            FacilityTable::class.java,
                                            JSONObject().apply {
                                                this.put("id", ObjectId())
                                                this.put("facilityId", facility.facilityID.toInt())
                                                this.put("facilityName", facility.name)
                                                this.put("optionId", option.id.toInt())
                                                this.put("optionIconName", option.icon)
                                                this.put("optionName", option.name)
                                            })

                                }
                            }
                            try {
                                if (db.isClosed.not()) {
                                    db.close()
                                }
                            } catch (e: Exception) {
                                Log.w(TAG, "DB close exception: $e")
                            }
                        }
                        Log.d(TAG, "success from API")
                        callbackResult.set(Result.success())
                    } catch (e: Exception) {
                        Log.e(TAG, "exception onSuccess from API $e")
                        callbackResult.set(Result.failure())
                    }


                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "error response from API $e")
                    callbackResult.set(Result.failure())
                }
            })
    }

    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture(callback)
    }

    override fun onStopped() {
        disposable?.let {
            if (it.isDisposed.not()) {
                try {
                    it.dispose()
                } catch (e: Exception) {
                    Log.w(TAG, "disposable exception: $e")
                }
            }
        }
        super.onStopped()
    }
}