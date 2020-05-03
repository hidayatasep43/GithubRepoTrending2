package com.example.githubrepotrending.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepotrending.R
import com.example.githubrepotrending.custom.CircleView
import com.example.githubrepotrending.data.model.GithubRepo
import com.example.githubrepotrending.utils.GithubRepoDiffCallBack
import com.squareup.picasso.Picasso

class GithubRepoAdapter internal constructor(
    context: Context,
    private val listener: EventItemListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val TYPE_COLLAPSED = 1
        val TYPE_EXPAND = 2
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var githubRepos = emptyList<GithubRepo>() // Cached copy of words

    class ViewHolderCollapse(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivGithubRepo: ImageView = itemView.findViewById(R.id.image_avatar);
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title);
        val tvName: TextView = itemView.findViewById(R.id.tv_name);

        fun bindItem(githubRepo: GithubRepo, eventItemListener: EventItemListener) {
            Picasso.get()
                .load(githubRepo.avatar)
                .into(ivGithubRepo)
            tvTitle.text = githubRepo.name
            tvName.text = githubRepo.author
            itemView.setOnClickListener {
                eventItemListener.onRepoClick(adapterPosition)
            }
        }
    }

    class ViewHolderExpand(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivGithubRepo: ImageView = itemView.findViewById(R.id.image_avatar);
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title);
        val tvName: TextView = itemView.findViewById(R.id.tv_name);
        val tvDesc: TextView = itemView.findViewById(R.id.tv_desc);
        val circleView: CircleView = itemView.findViewById(R.id.circle_language);
        val tvLanguage: TextView = itemView.findViewById(R.id.tv_language);
        val tvStar: TextView = itemView.findViewById(R.id.tv_star);
        val tvFork: TextView = itemView.findViewById(R.id.tv_fork);

        fun bindItem(githubRepo: GithubRepo, eventItemListener: EventItemListener) {
            Picasso.get()
                .load(githubRepo.avatar)
                .into(ivGithubRepo)
            tvTitle.text = githubRepo.name
            tvName.text = githubRepo.author
            if (githubRepo.language.isEmpty()) {
                circleView.visibility = GONE
                tvLanguage.visibility = GONE
            } else {
                circleView.visibility = VISIBLE
                tvLanguage.visibility = VISIBLE
                circleView.setColor(githubRepo.languageColor)
                tvLanguage.text = githubRepo.language
            }
            if (githubRepo.description.isEmpty()) {
                tvDesc.visibility = GONE
            } else {
                tvDesc.visibility = VISIBLE
                tvDesc.text = githubRepo.description
            }
            tvStar.text = githubRepo.stars.toString()
            tvFork.text = githubRepo.forks.toString()
            itemView.setOnClickListener {
                eventItemListener.onRepoClick(adapterPosition)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (githubRepos[position].isExpanded) {
            TYPE_EXPAND
        } else {
            TYPE_COLLAPSED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_EXPAND) {
            ViewHolderExpand(
                inflater.inflate(
                    R.layout.item_list_githubrepo_expanded,
                    parent,
                    false
                )
            )
        } else {
            ViewHolderCollapse(
                inflater.inflate(
                    R.layout.item_list_githubrepo,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return githubRepos.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_EXPAND) {
            val viewHolderExpand = holder as ViewHolderExpand
            viewHolderExpand.bindItem(githubRepos[position], listener)
        } else {
            val viewHolderCollapse = holder as ViewHolderCollapse
            viewHolderCollapse.bindItem(githubRepos[position], listener)
        }
    }

    internal fun setRepos(githubRepos: List<GithubRepo>?) {
        if (githubRepos != null) {
            this.githubRepos = githubRepos
            notifyDataSetChanged()
        }
    }

    internal fun updateRepo(position: Int) {
        val githubRepo = githubRepos.get(position)
        githubRepo.isExpanded =  !githubRepo.isExpanded
        notifyItemChanged(position)
    }

    interface EventItemListener {
        fun onRepoClick(position: Int)
    }


}