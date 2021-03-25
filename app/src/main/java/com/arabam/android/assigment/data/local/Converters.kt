package com.arabam.android.assigment.data.local

import androidx.room.TypeConverter
import com.arabam.android.assigment.data.model.Properties
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromPropertiesList(value: List<Properties>): String {
        val list = Types.newParameterizedType(
            MutableList::class.java,
            Properties::class.java
        )
        val moshiAdapter = moshi.adapter<List<Properties>>(list)
        return moshiAdapter.toJson(value)
    }

    @TypeConverter
    fun toPropertiesList(value: String): List<Properties>? {
        val list = Types.newParameterizedType(
            MutableList::class.java,
            Properties::class.java
        )
        val moshiAdapter = moshi.adapter<List<Properties>>(list)
        return moshiAdapter.fromJson(value)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val list = Types.newParameterizedType(
            MutableList::class.java,
            String::class.java
        )
        val moshiAdapter = moshi.adapter<List<String>>(list)
        return moshiAdapter.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val list = Types.newParameterizedType(
            MutableList::class.java,
            String::class.java
        )
        val moshiAdapter = moshi.adapter<List<String>>(list)
        return moshiAdapter.fromJson(value)
    }
}
