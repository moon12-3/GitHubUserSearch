package com.example.githubusersearch

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReposAdapter(val reposList : List<GithubRepos>) :
    RecyclerView.Adapter<ReposAdapter.ItemViewHolder>() {
    class ItemViewHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val number = reposList[position]
        val nameText = holder.view.findViewById<TextView>(R.id.name_text)
        val descriptionText = holder.view.findViewById<TextView>(R.id.description_text)
        val forksCnt = holder.view.findViewById<TextView>(R.id.forks_cnt)
        val watchCnt = holder.view.findViewById<TextView>(R.id.watchers_cnt)
        val starCnt = holder.view.findViewById<TextView>(R.id.star_cnt)
        val link = holder.view.findViewById<LinearLayout>(R.id.repos_item_id)

        nameText.text = number.name
        descriptionText.text = number.description
        forksCnt.text = number.forks_count.toString()
        watchCnt.text = number.watchers_count.toString()
        starCnt.text = number.stargazers_count.toString()

        link.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(number.html_url))
            holder.view.context.startActivity(intent)
        }

        if(number.description.isNullOrBlank()) {
            descriptionText.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = reposList.size

    override fun getItemViewType(position: Int): Int = R.layout.repos_item

}
