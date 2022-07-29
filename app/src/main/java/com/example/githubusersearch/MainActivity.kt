package com.example.githubusersearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
        val idText = findViewById<TextView>(R.id.id_result)

        searchBtn.setOnClickListener {
            ID_KEY = userIdText.text.toString()
            Log.d("mytag", ID_KEY)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().registerTypeAdapter(
                            GithubResponseGSON::class.java,
                            GithubResponseDeserializerGSON()
                        ).create()
                    )
                )
                .build()

            val apiService = retrofit.create(GitHubAPIService::class.java)
            val apiCallForData = apiService.getUser(ID_KEY)

            apiCallForData.enqueue(object : Callback<GithubResponseGSON> {
                override fun onResponse(
                    call: Call<GithubResponseGSON>,
                    response: Response<GithubResponseGSON>
                ) {
                    val data = response.body()
                    Log.d("my_tag", data.toString())

                    val id = data?.id
                    val login = data?.login

                    idText.text = "ID : "+ id.toString()
                    loginText.text = "login : " + login

                }

                override fun onFailure(call: Call<GithubResponseGSON>, t: Throwable) {
//                    Toast.makeText(this, "예외 처리 : ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })

        }


    }
}