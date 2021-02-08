package com.arabam.android.assigment.data.model

data class CarDetail(
    val id: Int,
    val title: String,
    val location: Location,
    val category: Category,
    val modelName: String,
    val priceFormatted: String,
    val dateFormatted: String,
    val photos: List<String>,
    val properties: List<Properties>,
    val text: String,
    val userInfo: UserInfo
)