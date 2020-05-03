package com.example.githubrepotrending.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubrepotrending.R
import com.example.githubrepotrending.data.model.Status
import com.example.githubrepotrending.viewmodel.GithubRepoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), GithubRepoAdapter.EventItemListener {

    private lateinit var githubRepoViewModel: GithubRepoViewModel
    private lateinit var adapter: GithubRepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupList()

        githubRepoViewModel = ViewModelProvider(this).get(GithubRepoViewModel::class.java)
        githubRepoViewModel._listGithub.observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                if (it.data.isNullOrEmpty() ) {
                    setupView(true)
                } else {
                    setupView(false)
                    adapter.setRepos(it.data)
                }
            } else if (it.status == Status.LOADING) {
                hideEmptyView()
                if (!swiperefresh.isRefreshing) {
                    progressbar.visibility = View.VISIBLE
                }
            } else {
                setupView(true)
                val snackbar = Snackbar
                    .make(coordinatorLayout, it.message.toString(), Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        })

        swiperefresh.setOnRefreshListener {
            githubRepoViewModel.getListGithubRepo()
        }

        btnRefresh.setOnClickListener({
            githubRepoViewModel.getListGithubRepo()
        })
    }

    private fun setupList() {
        adapter = GithubRepoAdapter(this, this)
        recyclerview.setLayoutManager(LinearLayoutManager(this))
        recyclerview.setItemAnimator(DefaultItemAnimator())
        recyclerview.setAdapter(adapter)
    }

    override fun onRepoClick(position: Int) {
        adapter.updateRepo(position)
    }

    private fun setupView(isEmptyState: Boolean) {
        dismissLoading()
        if (isEmptyState) {
            titleEmptyState.visibility = View.VISIBLE
            descEmptyState.visibility = View.VISIBLE
            imgEmptyState.visibility = View.VISIBLE
            btnRefresh.visibility = View.VISIBLE
            swiperefresh.visibility = View.GONE
        } else {
            hideEmptyView()
            swiperefresh.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyView() {
        if (btnRefresh.visibility == View.VISIBLE) {
            titleEmptyState.visibility = View.GONE
            descEmptyState.visibility = View.GONE
            imgEmptyState.visibility = View.GONE
            btnRefresh.visibility = View.GONE
        }
    }

    private fun dismissLoading() {
        progressbar.visibility = View.GONE
        swiperefresh.setRefreshing(false)
    }

}
