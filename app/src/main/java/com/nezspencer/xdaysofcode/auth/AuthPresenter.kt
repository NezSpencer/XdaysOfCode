package com.nezspencer.xdaysofcode.auth

import com.nezspencer.xdaysofcode.App
import com.nezspencer.xdaysofcode.GithubApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by reach1 on 1/22/18.
 */
class AuthPresenter(private val contract: AuthContract.VModel) : AuthContract.Presenter{


    var vModelContract = contract

    override fun checkIfUserExists(username: String) {
        vModelContract.showLoading()
        App.retrofit.create(GithubApi::class.java)
                .checkUser(username)
                .enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        vModelContract.hideLoading()
                        if (response!!.code() == 200)
                            vModelContract.onSuccess()
                        else
                            vModelContract.onError(response.message())
                    }


                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        vModelContract.hideLoading()
                        vModelContract.onError(t!!.message.toString())
                    }
                })
    }
}