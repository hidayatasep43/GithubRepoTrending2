package com.example.githubrepotrending.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubrepotrending.data.model.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType>(val viewModelScope: CoroutineScope) {

    private val result = MutableLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        viewModelScope.launch {
            if (shouldFetch()) {
                fetchFromNetwork()
            } else {
                val dbSource = loadFromDb()
                setValue(Resource.success(dbSource, null))
            }
        }
    }

    private suspend fun fetchFromNetwork() {
        val response = createCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body == null || response.code() == 204) {
                setValue(Resource.empty())
            } else {
                val resultResponse = processResponse(body)
                setValue(Resource.success(resultResponse, null))
                saveCallResult(resultResponse)
                saveLastUpdate()
            }
        } else {
            val msg = response.errorBody()?.string()
            val errorMsg = if (msg.isNullOrEmpty()) {
                response.message()
            } else {
                msg
            }
            if (isExpiredData()) {
                setValue(Resource.error(errorMsg, null))
            } else {
                viewModelScope.launch {
                    val dbSource = loadFromDb()
                    setValue(Resource.success(dbSource, msg))
                }
            }
        }
    }

    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected abstract suspend fun saveCallResult(item: ResultType)

    protected abstract fun processResponse(response:RequestType): ResultType

    abstract suspend fun createCall(): Response<RequestType>

    protected abstract fun saveLastUpdate()

    protected abstract fun shouldFetch(): Boolean

    protected abstract fun isExpiredData(): Boolean

    protected abstract suspend fun loadFromDb(): ResultType


}