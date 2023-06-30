package com.krupal.app_data.database.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class ExclusionTable : RealmObject() {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var facilityId: Int = 0
    var optionId: Int = 0
    var exclusionFacilityId: Int = 0
    var exclusionOptionId: Int = 0
}