package com.nezspencer.xdaysofcode.auth

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by reach1 on 1/22/18.
 */
class AuthViewModel : ViewModel(), AuthContract.VModel {

    private lateinit var presenter: AuthPresenter
    public lateinit var isUser: MutableLiveData<Boolean>
    lateinit var username : String
    fun setup(username: String){
        presenter = AuthPresenter(this)
        this.username = username
        isUser = MutableLiveData()
    }

    fun startJob(){
        presenter.checkIfUserExists(username)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onSuccess() {
        isUser.value = true
    }

    override fun onError(errorMsg: String) {
        isUser.value = false
    }
}