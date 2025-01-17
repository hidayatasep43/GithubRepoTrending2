package com.example.githubrepotrending.data.model

data class Resource<out T>(val status: Status, val data: T?, val message: String?){

    companion object {

        fun <T> success(data: T?, msg: String?): Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> empty(): Resource<T> {
            return Resource(Status.EMPTY, null, null)
        }

    }

}