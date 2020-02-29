package com.example.githubrepotrending.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.githubrepotrending.data.model.GithubRepo
import com.example.githubrepotrending.data.model.Resource
import com.example.githubrepotrending.data.source.GithubRepoRepository
import com.example.githubrepotrending.data.source.local.GithubRoomDatabase
import com.example.githubrepotrending.data.source.remote.GithubRepoServiceFactory
import com.example.githubrepotrending.utils.LocalPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GithubRepoViewModel(application: Application): AndroidViewModel(application) {

    private val githubRepoRepository: GithubRepoRepository

    private var listGithubRepo: LiveData<Resource<List<GithubRepo>>> = MutableLiveData()
    val _listGithub = MediatorLiveData<Resource<List<GithubRepo>>>()

    init {
        val githubRepoDao = GithubRoomDatabase.getDatabase(application).githubRepoDao()
        val localPreferences = LocalPreferences(application)
        githubRepoRepository = GithubRepoRepository(githubRepoDao, GithubRepoServiceFactory.getGithubRepoService(), localPreferences)

        getListGithubRepo()
    }

    fun getListGithubRepo() {
        viewModelScope.launch(Dispatchers.Main) {
            _listGithub.removeSource(listGithubRepo)
            listGithubRepo = githubRepoRepository.getGithubRepoV2()
            _listGithub.addSource(listGithubRepo) {
                _listGithub.value = it
            }
        }
    }

    /*public fun getListGithubRepo() {
        _listGithub.removeSource(listGithubRepo)
        listGithubRepo = githubRepoRepository.getGithubRepo(viewModelScope)
        _listGithub.addSource(listGithubRepo) {
            _listGithub.value = it
        }
    }*/



}