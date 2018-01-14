package com.nezspencer.xdaysofcode

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.nezspencer.xdaysofcode.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException
import java.math.BigInteger
import java.security.SecureRandom

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding;
    lateinit var firebaseAuth: FirebaseAuth;
    lateinit var authListener: FirebaseAuth.AuthStateListener
    private val random: SecureRandom = SecureRandom()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.wvGithub.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url!!.startsWith("xdaysofy://git.")){
                    val uri = Uri.parse(url)
                    val code = uri.getQueryParameter("code")
                    val state = uri.getQueryParameter("state")

                    getAccessToken(code,state)
                }
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        binding.btnGithubAuth.setOnClickListener { view: View -> authWithGithub() }

        authListener = FirebaseAuth.AuthStateListener { fbaseAuth->
            run {
                val user = fbaseAuth.currentUser
                if (user == null) {
                    Toast.makeText(this@MainActivity, "NOT Logged in!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Logged in!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        }

    private fun getAccessToken(code: String, state: String) {
        val okHttpClient = OkHttpClient()
        val form = FormBody.Builder()
                .add("client_id",getString(R.string.client_id))
                .add("client_secret",getString(R.string.client_secret))
                .add("code",code)
                .add("state",state)
                .add("redirect_uri",getString(R.string.redirect_uri))
                .build()
        val request = Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(form)
                .build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e!!.printStackTrace()
                Toast.makeText(this@MainActivity,"Failure: ${e.toString()}"
                        ,Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call?, response: Response?) {
                //access_token=e72e16c7e42f292c6912e7710c838347ae178b4a&token_type=bearer
                val responseString = response!!.body().toString()
                val splitted = responseString.split("=|&")
                if (splitted[0].equals("access_token",true))
                    firebaseAuthWithToken(splitted[1])

            }
        })
    }

    private fun firebaseAuthWithToken(s: String) {
        val githubCredential = GithubAuthProvider.getCredential(s)
        firebaseAuth.signInWithCredential(githubCredential)
                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + p0.isSuccessful());

                        if (!p0.isSuccessful()) {
                            p0.getException()!!.printStackTrace();
                            Log.w(TAG, "signInWithCredential", p0.getException());
                            Toast.makeText(this@MainActivity, "Authentication failed.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                })
    }

    private fun authWithGithub() {
        binding.wvGithub.visibility = View.VISIBLE
        binding.btnGithubAuth.visibility = View.GONE
        val httpUrl = HttpUrl.Builder()
                .scheme("http")
                .host("github.com")
                .addPathSegment("login")
                .addPathSegment("oauth")
                .addPathSegment("authorize")
                .addQueryParameter("client_id",getString(R.string.client_id))
                //.addQueryParameter("redirect_uri",getString(R.string.redirect_uri))
                .addQueryParameter("state",randomString())
                .addQueryParameter("scope","read:user")
                .build()

        binding.wvGithub.loadUrl(httpUrl.toString())
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener { authListener }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener { authListener }
    }

    private fun randomString(): String = BigInteger(130, random).toString(32)

    companion object {

        private val TAG = MainActivity::class.java.simpleName
    }
}

