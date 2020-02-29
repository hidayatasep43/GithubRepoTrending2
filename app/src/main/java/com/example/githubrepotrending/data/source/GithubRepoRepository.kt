package com.example.githubrepotrending.data.source

import androidx.lifecycle.LiveData
import com.example.githubrepotrending.data.model.GithubRepo
import com.example.githubrepotrending.data.model.Resource
import com.example.githubrepotrending.data.source.local.GithubRepoDao
import com.example.githubrepotrending.data.source.local.GithubRoomDatabase
import com.example.githubrepotrending.data.source.remote.ApiResponse
import com.example.githubrepotrending.data.source.remote.GithubRepoService
import com.example.githubrepotrending.utils.LocalPreferences
import kotlinx.coroutines.CoroutineScope
import retrofit2.Response
import java.util.*

class GithubRepoRepository(
    private val githubRepoDao: GithubRepoDao,
    private val githubRepoService: GithubRepoService,
    private val localPreferences: LocalPreferences) {

    suspend fun getGithubRepoV2(): LiveData<Resource<List<GithubRepo>>> {
        return object : NetworkBoundResourceV2<List<GithubRepo>, List<GithubRepo>>() {

            override fun processResponse(response: List<GithubRepo>): List<GithubRepo> {
                return response
            }

            override suspend fun saveCallResult(item: List<GithubRepo>) {
                githubRepoDao.setGithubRepoData(item)
            }

            override suspend fun createCall(): Response<List<GithubRepo>> {
                return githubRepoService.getPopularGithubRepo()
            }

            override fun saveLastUpdate() {
                localPreferences.save(
                    LocalPreferences.Key.LAST_UPDATE_REPO.name, Calendar.getInstance().timeInMillis
                )
            }

            override fun shouldFetch(data: List<GithubRepo>?): Boolean {
                return true
            }

            override fun isExpiredData(): Boolean {
                val lastUpdate: Long =
                    localPreferences.getValueLong(LocalPreferences.Key.LAST_UPDATE_REPO.name)
                //max time data fecth from database is 2 hour from last saved
                return Calendar.getInstance().timeInMillis - lastUpdate > 7200000
            }

            override suspend fun loadFromDb(): List<GithubRepo> {
                return githubRepoDao.getAllRepo()
            }

        }.build().asLiveData()
    }


}