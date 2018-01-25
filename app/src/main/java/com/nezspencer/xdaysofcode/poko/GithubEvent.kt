package com.nezspencer.xdaysofcode.poko

import com.google.gson.annotations.SerializedName

data class GithubEvent(val actor: Actor?,
                       val public: Boolean = false,
                       val payload: Payload?,
                       val repo: Repo?,
                       @SerializedName("created_at")
                       val createdAt: String = "",
                       val id: String = "",
                       val type: String = "")