package com.arabam.android.assigment.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_table")
data class CarEntity(
    @PrimaryKey val carId: Long,
    val title: String?,
    val cityName: String?,
    val townName: String?,
    val categoryId: Long?,
    val categoryName: String?,
    val priceFormatted: String?,
    val photo: String
)

@Entity(tableName = "skip_table")
data class SkipEntity(
    val skip:Int = 0,
    @PrimaryKey(autoGenerate = true) var Id:Int = 0
)

@Entity(tableName = "car_detail_table")
data class CarDetailEntity(
    @PrimaryKey val carId: Long,
    val title: String,
    val cityName: String?,
    val townName: String?,
    val categoryId: Long?,
    val categoryName: String?,
    val modelName: String?,
    val priceFormatted: String?,
    val dateFormatted: String?,
    val photos: List<String>?,
    val properties: List<Properties>?,
    val text: String?,
    val userId: Int,
    val nameSurname: String,
    val phoneFormatted: String,
    val phone: String,
    val updatedAt: Long?
)