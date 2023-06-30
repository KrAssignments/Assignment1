package com.krupal.assignmentradiusagent

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.krupal.app_domain.entity.ExclusionEntity
import com.krupal.app_domain.entity.FacilityEntity

import com.krupal.assignmentradiusagent.databinding.ActivityMainBinding
import com.krupal.app_domain.presenter.FacilitiesPresenter
import com.krupal.app_domain.presenter.FacilitiesContract

class MainActivity : AppCompatActivity(), FacilitiesContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainListAdapter
    private val presenter: FacilitiesPresenter by lazy { FacilitiesPresenter(application) }
    private val propertyType = "Property Type"
    private var facilityId : Int? = null
    private var optionId : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        adapter = MainListAdapter(this)
        presenter.setView(this)

        with(binding){
            rList.layoutManager = LinearLayoutManager(this@MainActivity)
            rList.adapter = adapter
            btReset.setOnClickListener {
                adapter.resetSelection()
            }
            btSubmit.setOnClickListener {
                adapter.resetSelection()
            }
        }

        generateRequest(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_reload -> {
                generateRequest(true)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun generateRequest(force: Boolean) {
        presenter.getFacilitiesFromRemote(force)
        presenter.getFacilitiesFromLocal(type = propertyType)
    }

    override fun showProgressDialog(showProgress: Boolean) {
        with(binding) {
            progressCard.isVisible = showProgress
            rList.isVisible = showProgress.not()
        }

    }

    override fun showError(message: CharSequence?) {
        Snackbar.make(binding.content, "Error: $message", Snackbar.LENGTH_SHORT).show()
    }

    override fun onFacilitiesLoaded(
        facilityTable: List<FacilityEntity>,
        exclusionList: List<ExclusionEntity>
    ) {
        adapter.updateData(facilityTable, exclusionList)
    }

    override fun onFacilityLoadFailure(exception: Exception) {
        adapter.updateData(arrayListOf(), arrayListOf())
        showError(exception.message)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}