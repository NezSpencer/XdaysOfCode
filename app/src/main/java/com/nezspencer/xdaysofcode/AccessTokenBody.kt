package com.nezspencer.xdaysofcode

/**
 * Created by reach1 on 1/19/18.
 */
data class AccessTokenBody(val client_id:String, val client_secret:String, val code: String, val redirect_uri: String)