package com.krupal.assignmentradiusagent

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.krupal.assignmentradiusagent.database.model.FacilityTable
import com.krupal.assignmentradiusagent.databinding.ActivityMainBinding
import com.krupal.assignmentradiusagent.presenter.MainContract
import com.krupal.assignmentradiusagent.service.FacilitiesDownloadService

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainListAdapter
    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        adapter = MainListAdapter(this)

        presenter = object : MainContract
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_reload -> {
                generateRequest()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun generateRequest() {
        val request = OneTimeWorkRequest.Builder(FacilitiesDownloadService::class.java)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(this)
            .enqueueUniqueWork("request", ExistingWorkPolicy.REPLACE, request)
    }

    override fun showProgressDialog(showProgress: Boolean) {
        with(binding){
            progressCard.isVisible = showProgress
            rList.isVisible = showProgress.not()
            text.isVisible = showProgress.not()
        }

    }
    override fun showError(message: CharSequence?) {
        Snackbar.make(binding.content, "Error: $message", Snackbar.LENGTH_SHORT).show()
    }

    override fun onFacilitiesLoaded(facilityTable: List<FacilityTable>) {
        adapter.updateData(facilityTable)
    }

    override fun onFacilityLoadFailure(exception: Exception) {
        adapter.updateData(arrayListOf())
        showError(exception.message)
    }

}