package com.krupal.assignmentradiusagent.service

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.krupal.assignmentradiusagent.database.AppDb
import com.krupal.assignmentradiusagent.network.ApiService
import com.krupal.assignmentradiusagent.network.RetrofitClient
import com.krupal.assignmentradiusagent.network.model.APIResponse
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.realm.kotlin.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class FacilitiesDownloadService constructor(context: Context, params: WorkerParameters) :
    ListenableWorker(context, params) {

    private val TAG: String = FacilitiesDownloadService::class.java.simpleName
    private val apiService: ApiService = RetrofitClient.client.create(ApiService::class.java)
    private val db: Realm = Realm.open(AppDb.appDBConfig)

    private var disposable: Disposable? = null
    private val job = CoroutineScope(Dispatchers.IO)

    private val callback = CallbackToFutureAdapter.Resolver<Result> {
        apiService.getData().subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
            .subscribe(object : SingleObserver<APIResponse> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onSuccess(t: APIResponse) {
                    Log.d(TAG, "success response from API $t")
//                    job.launch {
//                        db.write {
//                            this.deleteAll()
//
//                        }
//                    }


                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "error response from API $e")
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
        try {
            if (db.isClosed().not()) {
                db.close()
            }
        } catch (e: Exception) {
            Log.w(TAG, "DB close exception: $e")
        }
        super.onStopped()
    }
}