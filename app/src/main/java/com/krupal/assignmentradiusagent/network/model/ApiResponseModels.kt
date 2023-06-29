package com.krupal.assignmentradiusagent.network.model

import com.google.gson.annotations.SerializedName


data class APIResponse (
    @SerializedName("facilities")
    val facilities: List<Facility>,
    @SerializedName("exclusions")
    val exclusions: List<List<Exclusion>>
)

data class Exclusion (
    @SerializedName("facility_id")
    val facilityID: String,

    @SerializedName("options_id")
    val optionsID: String
)

data class Facility (
    @SerializedName("facility_id")
    val facilityID: String,

    val name: String,
    val options: List<Option>
)

data class Option (
    val name: String,
    val icon: String,
    val id: String
)
