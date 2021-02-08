package com.arabam.android.assigment.data.model

data class ErrorResponse(
    val type: String,
    val title: String,
    val status: Int,
    val traceId: String,
    val errors: Map<String, String>
)