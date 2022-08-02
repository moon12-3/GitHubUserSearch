package com.example.githubusersearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var ID_KEY : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userIdText = findViewById<EditText>(R.id.id_text)
        val searchBtn = findViewById<Button>(R.id.search_btn)
        val loginText = findViewById<TextView>(R.id.login_result)
        val profileImg = findViewById<ImageView>(R.id.profile_image)
        val saveListBtn = findViewById<Button>(R.id.save_list_btn)

        saveListBtn.visibility = View.GONE

        val TOKEN = "token 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"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(
                GsonConverterFactory.create(
                    /*GsonBuilder().registerTypeAdapter(
                        GithubResponseGSON::class.java,
                        GithubResponseDeserializerGSON()
                    ).create()*/
                )
            )
            .build()
        val apiService = retrofit.create(GitHubAPIService::class.java)
        // val classInfo : Class<GitHubAPIService> = GitHubAPIService::class.java 해당 클래스 정보를 얻을 수 있다??

        saveListBtn.setOnClickListener {
            if(userIdText.text.toString().isBlank())
                Toast.makeText(this, "유저 아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
            else {
                val intent = Intent(this, GitHubUserRepositoryListActivity::class.java)
                intent.putExtra("id", userIdText.text.toString())
                intent.putExtra("token", TOKEN)
                startActivity(intent)
            }
        }

        searchBtn.setOnClickListener {
            ID_KEY = userIdText.text.toString()
            Log.d("mytag", ID_KEY)

            val apiCallForData = apiService.getUser(ID_KEY, TOKEN)

            apiCallForData.enqueue(object : Callback<GithubResponseGSON> {
                override fun onResponse(
                    call: Call<GithubResponseGSON>,
                    response: Response<GithubResponseGSON>
                ) {
                    val errorId = response.code().toString()
                    Log.d("mytag", errorId)
                    if(errorId.startsWith("4")) {
                        Toast.makeText(this@MainActivity, "유저가 없습니다.", Toast.LENGTH_SHORT).show()
                        saveListBtn.visibility = View.GONE
                        loginText.visibility = View.GONE
                        profileImg.visibility = View.GONE
                    }
                    else {
                        saveListBtn.visibility = View.VISIBLE
                        loginText.visibility = View.VISIBLE
                        profileImg.visibility = View.VISIBLE
                        val data = response.body()
//                    Log.d("my_tag", data.toString())

                        val id = data?.id
                        val login = data?.login
                        val name = data?.name
                        val following = data?.following
                        val followers = data?.followers
                        val avatarUrl = data?.avatarUrl

                        Glide.with(this@MainActivity).load(avatarUrl).into(profileImg)
                        loginText.text =
                            "ID : $id  login : $login \nname : $name \nfollowing : $following  follower : $followers"
                    }
                }

                override fun onFailure(call: Call<GithubResponseGSON>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "예외 처리 : ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })

        }


    }
}