package com.example.githubusersearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubPageFragment : Fragment() {
    var ID_KEY : String = "moon12-3"
    lateinit var loginText : TextView
    lateinit var idText : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.github_page_fragment, container, false)
        val userIdText = view.findViewById<EditText>(R.id.id_text)
        val searchBtn = view.findViewById<Button>(R.id.search_btn)
        loginText = view.findViewById<TextView>(R.id.login_result)
        idText = view.findViewById<TextView>(R.id.id_result)

        searchBtn.setOnClickListener {
            ID_KEY = idText.text.toString()
            Log.d("mytag", ID_KEY)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                Toast.makeText(activity, "예외 처리 : ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })

    }
}