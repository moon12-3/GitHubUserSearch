package com.example.githubusersearch

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.lang.reflect.Type

interface GitHubAPIService {
    @GET("/users/{userId}")
    fun getUser(
        @Path("userId") login: String,
        @Header("Authorization") pat : String
    ): Call<GithubResponseGSON>

    @GET("/users/{userId}/repos")
    fun getRepos(
        @Path("userId") userId: String,
        @Header("Authorization") pat : String
    ) : Call<List<GithubRepos>>
}

data class GithubRepos(
    val name : String,
    val html_url : String,
    val description : String,
    val forks_count : Int,
    val watchers_count : Int,
    val stargazers_count : Int
)

data class GithubResponseGSON(
    val id : Int,
    val login : String,
    val name : String?,
    var followers : Int,
    var following : Int,
    @SerializedName("avatar_url")   // 원래 이름
    val avatarUrl : String) // 이름을 바꿀 수 있음 (새로 부여한 이름)

/*
class GithubResponseDeserializerGSON : JsonDeserializer<GithubResponseGSON> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GithubResponseGSON {
        val node = json?.asJsonObject
        val id = node?.getAsJsonPrimitive("id")?.asInt
        val login = node?.getAsJsonPrimitive("login")?.asString

        return GithubResponseGSON(
            id!!,
            login!!
        )
    }

}*/