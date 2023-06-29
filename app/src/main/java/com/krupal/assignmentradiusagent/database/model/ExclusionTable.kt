package com.krupal.assignmentradiusagent.database.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ExclusionTable constructor() : RealmObject {
    @PrimaryKey
    var id : ObjectId = ObjectId()
    var facilityId : Int = 0
    var optionId : Int = 0
    var exclusionFacilityId : Int = 0
    var exclusionOptionId: Int = 0
}