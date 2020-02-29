package com.example.githubrepotrending.data.source.remote

import androidx.lifecycle.LiveData
import com.example.githubrepotrending.data.model.GithubRepo
import retrofit2.Response
import retrofit2.http.GET

interface GithubRepoService {

    @GET("repositories")
    suspend fun getPopularGithubRepo(): Response<List<GithubRepo>>

}