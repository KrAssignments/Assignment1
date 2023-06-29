package com.krupal.assignmentradiusagent.database.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class FacilityTable constructor() : RealmObject {
    @PrimaryKey
    var id : ObjectId = BsonObjectId()
    var facilityId : Int = 0
    var facilityName: String = ""
    var optionId: Int = 0
    var optionName: String = ""
    var optionIconName : String = ""
}
