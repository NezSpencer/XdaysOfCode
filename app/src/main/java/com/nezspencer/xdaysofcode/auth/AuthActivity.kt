package com.nezspencer.xdaysofcode.auth

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.nezspencer.xdaysofcode.R
import com.nezspencer.xdaysofcode.dashboard.TrackerActivity
import com.nezspencer.xdaysofcode.databinding.ActivityAuthBinding

/**
 * Created by reach1 on 1/22/18.
 */
class AuthActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

        binding.btnAuth.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val username = binding.etUsername.text.toString()
                viewModel.setup(username)
                viewModel.startJob()
                viewModel.isUser.observe(this@AuthActivity, object: Observer<Boolean>{
                    override fun onChanged(t: Boolean?) {

                        if (t ?: true)
                            startDashBoardActivity()
                        else
                            Toast.makeText(this@AuthActivity,"Username not found",Toast.LENGTH_SHORT).show()
                    }
                })

            }
        })
    }

    private fun startDashBoardActivity() {
        Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
        startActivity(TrackerActivity.newIntent(this))
    }
}