package com.opentable.openfoods.common

sealed class OFResult<out T> {
    data class Success<out T>(val data: T) : OFResult<T>()
    data class Error(val exception: Exception) : OFResult<Nothing>()
}