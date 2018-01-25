package com.nezspencer.xdaysofcode.dashboard

import com.nezspencer.xdaysofcode.poko.Api

/**
 * Created by reach1 on 1/21/18.
 */
interface TrackerContract {

    interface VModel{

        fun loading()
        fun done()
        fun onSuccess(api: Api)
        fun onError(msg: String)
    }

    interface Presenter{
        fun getGithubActivity(username: String)
    }
}