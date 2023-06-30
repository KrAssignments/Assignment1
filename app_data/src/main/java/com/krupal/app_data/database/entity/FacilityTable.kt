package com.krupal.app_data.database.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class FacilityTable : RealmObject() {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var facilityId: Int = 0
    var facilityName: String = ""
    var optionId: Int = 0
    var optionName: String = ""
    var optionIconName: String = ""
}
