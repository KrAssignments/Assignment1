package com.krupal.app_domain.presenter

import com.krupal.app_domain.base.BasePresenter
import com.krupal.app_domain.base.BaseView
import com.krupal.app_domain.entity.ExclusionEntity
import com.krupal.app_domain.entity.FacilityEntity

interface FacilitiesContract {

    interface View : BaseView {
        fun onFacilitiesLoaded(
            facilityTable: List<FacilityEntity>,
            exclusionList: List<ExclusionEntity>
        )
        fun onFacilityLoadFailure(exception: Exception)
    }
    interface Presenter : BasePresenter<View> {
        fun getFacilitiesFromLocal(type:String, facilityId:Int?= null, optionId: Int? = null)
        fun getFacilitiesFromRemote(forceUpdate: Boolean)
    }
}