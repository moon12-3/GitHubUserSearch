package com.example.githubusersearch

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Type

interface GitHubAPIService {
    @GET("/users/{login}")
    fun getUser(
        @Path("login") login : String
    ): Call<GithubResponseGSON>
}

data class GithubResponseGSON(val id : Int, val login : String)

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

}