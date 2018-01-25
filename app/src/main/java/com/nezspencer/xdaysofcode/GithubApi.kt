package com.nezspencer.xdaysofcode

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by reach1 on 1/19/18.
 */
interface GithubApi {

    @POST("login/oauth/access_token")
    fun getAccessToken(@Body body: AccessTokenBody) : Call<ResponseBody>

    @GET("users/{user}")
    fun checkUser(@Path("user") user: String): Call<ResponseBody>
}