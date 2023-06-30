package com.krupal.app_domain.entity

data class ExclusionEntity(
    var id : Int,
    var facilityId : Int,
    var optionId : Int,
    var exclusionFacilityId : Int,
    var exclusionOptionId: Int,
)
