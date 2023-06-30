package com.krupal.assignmentradiusagent.presenter

import com.krupal.assignmentradiusagent.base.BasePresenter
import com.krupal.assignmentradiusagent.base.BaseView
import com.krupal.assignmentradiusagent.database.model.FacilityTable
import java.lang.Exception

interface MainContract {

    interface View : BaseView{
        fun onFacilitiesLoaded(facilityTable: List<FacilityTable>)
        fun onFacilityLoadFailure(exception: Exception)
    }
    interface Presenter : BasePresenter<View>{
        fun getFacilitiesFromRemote()

        fun getFacilitiesFromLocal(type:String, facilityId:Int, optionId: Int)
    }
}