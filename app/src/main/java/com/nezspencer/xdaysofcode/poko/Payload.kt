package com.nezspencer.xdaysofcode.poko

import com.google.gson.annotations.SerializedName

data class Payload(val head: String = "",
                   val ref: String = "",
                   val size: Int = 0,
                   val before: String = "",
                   @SerializedName("push_id")
                   val pushId: Long = 0,
                   @SerializedName("distinct_size")
                   val distinctSize: Int = 0,
                   val commits: List<CommitsItem>?)