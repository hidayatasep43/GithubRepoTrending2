package com.example.githubrepotrending.data.source

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubrepotrending.data.model.Resource
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.coroutineContext

abstract class NetworkBoundResourceV2<ResultType, RequestType>() {

    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): NetworkBoundResourceV2<ResultType, RequestType> {
        withContext(Dispatchers.Main) {
            result.value = Resource.loading(null)
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            val dbResult = loadFromDb()
            if (shouldFetch(dbResult)){
                try {
                    fetchFromNetwork(dbResult)
                } catch (e:Exception) {
                    setResponseFromDB(e.toString(), dbResult)
                }
            } else {
                setValue(Resource.success(dbResult, null))
            }
        }
        return this
    }

    private suspend fun fetchFromNetwork(dbResult: ResultType) {
        setValue(Resource.loading(dbResult))
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
            setResponseFromDB(errorMsg, dbResult)
        }
    }

    private fun setResponseFromDB(errorMsg: String, dbResult: ResultType) {
        if (isExpiredData()) {
            setValue(Resource.error(errorMsg, null))
        } else {
            setValue(Resource.success(dbResult, errorMsg))
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    @WorkerThread
    protected abstract fun processResponse(response:RequestType): ResultType

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected abstract suspend fun saveCallResult(item: ResultType)

    @MainThread
    abstract suspend fun createCall(): Response<RequestType>

    @MainThread
    protected abstract fun saveLastUpdate()

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun isExpiredData(): Boolean

    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType


}