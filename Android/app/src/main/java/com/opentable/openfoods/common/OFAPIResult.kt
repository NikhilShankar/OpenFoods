package com.opentable.openfoods.common

sealed class OFAPIResult<out T> {

    data class Success<out T>(val data: T) : OFAPIResult<T>()
    data class Error(val message: String, val exception: Exception? = null) : OFAPIResult<Nothing>()
    object Loading : OFAPIResult<Nothing>()

}