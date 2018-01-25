package com.nezspencer.xdaysofcode.poko

data class CommitsItem(val author: Author?,
                       val distinct: Boolean = false,
                       val message: String = "",
                       val sha: String = "",
                       val url: String = "")