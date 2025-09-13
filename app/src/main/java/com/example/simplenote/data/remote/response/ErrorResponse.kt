package com.example.simplenote.data.remote.response

data class ErrorResponse(
    val type: String?,
    val errors: List<ErrorDetail>?
)

data class ErrorDetail(
    val attr: String?,
    val code: String?,
    val detail: String?
)