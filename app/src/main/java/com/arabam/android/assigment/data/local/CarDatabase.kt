package com.arabam.android.assigment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arabam.android.assigment.data.model.CarDetailEntity
import com.arabam.android.assigment.data.model.CarEntity
import com.arabam.android.assigment.data.model.SkipEntity

@Database(
    entities = [CarEntity::class, SkipEntity::class, CarDetailEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CarDatabase: RoomDatabase() {

    abstract fun carDao(): CarListDao

    abstract fun carDetailDao(): CarDetailDao
}