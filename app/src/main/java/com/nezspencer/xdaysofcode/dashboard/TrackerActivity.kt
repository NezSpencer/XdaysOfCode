package com.nezspencer.xdaysofcode.dashboard

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nezspencer.xdaysofcode.R
import com.nezspencer.xdaysofcode.databinding.ActivityTrackerBinding

/**
 * Created by reach1 on 1/21/18.
 */
class TrackerActivity : AppCompatActivity(){

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TrackerActivity::class.java)
        }
    }

    lateinit var binding: ActivityTrackerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tracker)
        

    }
}