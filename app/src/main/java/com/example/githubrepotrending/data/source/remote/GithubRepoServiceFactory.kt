package com.example.githubrepotrending.data.source.remote

import androidx.lifecycle.LiveData
import com.example.githubrepotrending.BuildConfig
import com.example.githubrepotrending.data.model.GithubRepo
import com.rifqimfahmi.foorballapps.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubRepoServiceFactory {

    companion object {

        @Volatile
        private var INSTANCE: GithubRepoService? = null

        fun getGithubRepoService(): GithubRepoService {
            return INSTANCE ?: synchronized(this) {
                val instance = Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(provideOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GithubRepoService::class.java)
                INSTANCE = instance
                instance
            }

        }

        private fun provideOkHttpClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
            client.addInterceptor(interceptor)
            return client.build()
        }



    }

}