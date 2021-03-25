package com.arabam.android.assigment.data.model

data class Car(
    val id: Long,
    val title: String,
    val priceFormatted: String,
    val photo: String,
    val location: Location,
    val category: Category,
)