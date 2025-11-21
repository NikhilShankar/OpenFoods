package com.opentable.openfoods.common

//Not being used currently as the apis are either used via
//pagingsource or are just simple apis with success true or false.
//Not removing since it will be used for any new apis being used.
sealed class OFResult<out T> {
    data class Success<out T>(val data: T) : OFResult<T>()
    data class Error(val exception: Exception) : OFResult<Nothing>()
}