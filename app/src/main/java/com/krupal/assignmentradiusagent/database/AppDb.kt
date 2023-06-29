package com.krupal.assignmentradiusagent.database

import com.krupal.assignmentradiusagent.database.model.ExclusionTable
import com.krupal.assignmentradiusagent.database.model.FacilityTable
import io.realm.kotlin.RealmConfiguration

object AppDb {
    val appDBConfig : RealmConfiguration = RealmConfiguration.Builder(
        setOf(FacilityTable::class, ExclusionTable::class)
    ).name("database.realm").build()
}