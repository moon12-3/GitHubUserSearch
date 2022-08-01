package com.example.githubusersearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GitHubUserRepositoryListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_hub_user_repository_list)


        val id = intent.getStringExtra("id")!!

        Toast.makeText(this, "id : $id", Toast.LENGTH_SHORT).show()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
        val apiService = retrofit.create(GitHubAPIService::class.java)

        val TOKEN = "token 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"
        val apiCallForData = apiService.getRepos(id, TOKEN)

        apiCallForData.enqueue(object : Callback<List<GithubRepos>> {
            override fun onResponse(call: Call<List<GithubRepos>>, response: Response<List<GithubRepos>>) {

                val errorId = response.code().toString()

                if(errorId.startsWith("4"))
                    Toast.makeText(this@GitHubUserRepositoryListActivity, "유저가 없습니다.", Toast.LENGTH_SHORT).show()
                else {
                    val data = response.body()
                    val reposList = mutableListOf<GithubRepos>()
                    for(i in 0 until data!!.size) reposList.add(data.get(i))

                    val layoutManager = LinearLayoutManager(this@GitHubUserRepositoryListActivity)

                    val adapter = ReposAdapter(data)

                    val recyclerView = findViewById<RecyclerView>(R.id.repos_list)

                    recyclerView.setHasFixedSize(false)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<GithubRepos>>, t: Throwable) {
                Toast.makeText(this@GitHubUserRepositoryListActivity, "에러 : ${t.message}", Toast.LENGTH_LONG).show()
                Log.d("mytag", "${t.message}")
            }


        })

    }
}