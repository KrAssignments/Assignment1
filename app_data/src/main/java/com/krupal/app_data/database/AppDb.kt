package com.krupal.app_data.database

import io.realm.RealmConfiguration

object AppDb {
    val appDBConfig: RealmConfiguration = RealmConfiguration
        .Builder()
        .name("database.realm.db")
        .deleteRealmIfMigrationNeeded()
        .schemaVersion(1)
        .build()
}