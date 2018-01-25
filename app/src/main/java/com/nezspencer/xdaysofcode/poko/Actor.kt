package com.nezspencer.xdaysofcode.poko

import com.google.gson.annotations.SerializedName

data class Actor(@SerializedName("display_login")
                 val displayLogin: String = "",
                 @SerializedName("avatar_url")
                 val avatarUrl: String = "",
                 val id: Int = 0,
                 val login: String = "",
                 @SerializedName("gravatar_id")
                 val gravatarId: String = "",
                 val url: String = "")