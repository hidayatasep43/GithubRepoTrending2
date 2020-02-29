package com.example.githubrepotrending.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.githubrepotrending.data.model.GithubRepo

class GithubRepoDiffCallBack(
    private val oldList: List<GithubRepo>,
    private val newList: List<GithubRepo>
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name.equals(newList[newItemPosition].name)
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].author.equals(newList[newItemPosition].author) &&
                oldList[oldItemPosition].name.equals(newList[newItemPosition].name) &&
                oldList[oldItemPosition].avatar.equals(newList[newItemPosition].avatar) &&
                oldList[oldItemPosition].description.equals(newList[newItemPosition].description) &&
                oldList[oldItemPosition].language.equals(newList[newItemPosition].language) &&
                oldList[oldItemPosition].languageColor.equals(newList[newItemPosition].languageColor) &&
                oldList[oldItemPosition].stars == newList[newItemPosition].stars &&
                oldList[oldItemPosition].forks == newList[newItemPosition].forks)
    }

}