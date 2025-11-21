package com.opentable.openfoods.common


//Not being used currently as the apis are either used via
//pagingsource or are just simple apis with success true or false.
//Not removing since it will be used for any new apis being used.
sealed class OFAPIResult<out T> {

    data class Success<out T>(val data: T) : OFAPIResult<T>()
    data class Error(val message: String, val exception: Exception? = null) : OFAPIResult<Nothing>()
    object Loading : OFAPIResult<Nothing>()

}