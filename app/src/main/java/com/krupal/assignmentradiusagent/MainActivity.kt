package com.krupal.assignmentradiusagent

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.krupal.assignmentradiusagent.databinding.ActivityMainBinding
import com.krupal.assignmentradiusagent.service.FacilitiesDownloadService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
}