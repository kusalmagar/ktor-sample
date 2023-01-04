package com.example.database.utils

import kotlinx.serialization.Serializable


@Serializable
data class Response<T>(
    val response: T,
    val success: Boolean,
)

