package com.arabam.android.assigment.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.arabam.android.assigment.data.model.CarEntity
import com.arabam.android.assigment.data.model.SkipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(carEntity: List<CarEntity>)

    @Query("DELETE FROM car_table")
    fun deleteAllCars()

    @Query("SELECT * FROM car_table")
    fun getAllCarsPaged(): PagingSource<Int, CarEntity>

    @Query("SELECT * FROM car_table")
    fun getAllCars(): Flow<List<CarEntity>>

    @Query("SELECT MAX(skip) FROM skip_table")
    fun getLastSkip():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkip(skip: SkipEntity)

    @Query("DELETE FROM skip_table")
    fun deleteAllSkips()
}