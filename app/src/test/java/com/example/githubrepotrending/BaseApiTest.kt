package com.example.githubrepotrending

import android.util.Log
import com.example.githubrepotrending.data.source.remote.GithubRepoService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.HttpURLConnection

class BaseApiTest {

    private var mockWebServer = MockWebServer()
    private lateinit var githubRepoService: GithubRepoService

    @Before
    fun setup() {
        mockWebServer.start()
        githubRepoService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
            .create(GithubRepoService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testResponse() {
        mockHttpResponse(mockWebServer, "test.json", HttpURLConnection.HTTP_OK)
        runBlocking {
            val response = githubRepoService.getPopularGithubRepo()
            val listRepo = response.body();
            if (listRepo != null) {
                assertEquals(25, listRepo.size)
                assertEquals("podgorskiy", listRepo.first().author)
                assertEquals("ALAE", listRepo.first().name)
                assertEquals("https://github.com/podgorskiy.png", listRepo.first().avatar)
            }
        }
    }

    @Test(expected = HttpException::class)
    fun testResponseFail(){
        mockHttpResponse(mockWebServer, "test.json", HttpURLConnection.HTTP_FORBIDDEN)
        runBlocking {
            val response = githubRepoService.getPopularGithubRepo()
            Log.e("","")
        }
    }

    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        return client.build()
    }

    fun mockHttpResponse(mockServer: MockWebServer, fileName: String, responseCode: Int) =
        mockServer.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(getJson(fileName))
        )

    fun getJson(path: String): String {
        val uri = this.javaClass.classLoader!!.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

}