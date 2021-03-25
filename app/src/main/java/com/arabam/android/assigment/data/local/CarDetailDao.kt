package com.arabam.android.assigment.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arabam.android.assigment.data.model.CarDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarDetail(carDetail: CarDetailEntity)

    @Query("SELECT * FROM car_detail_table WHERE carId = :id")
    fun getCarDetail(id: Long): Flow<CarDetailEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM car_detail_table WHERE carId = :id)")
    fun checkIdIfExist(id: Long): Boolean
}