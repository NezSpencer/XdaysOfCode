package com.nezspencer.xdaysofcode.auth

/**
 * Created by reach1 on 1/22/18.
 */
interface AuthContract {

    interface VModel {
        fun showLoading()
        fun hideLoading()
        fun onSuccess()
        fun onError(errorMsg: String)
    }

    interface Presenter{
        fun checkIfUserExists(username: String)
    }
}