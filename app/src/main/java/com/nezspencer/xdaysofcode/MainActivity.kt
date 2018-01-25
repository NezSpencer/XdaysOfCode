package com.nezspencer.xdaysofcode

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.nezspencer.xdaysofcode.databinding.ActivityMainBinding
import okhttp3.HttpUrl
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
        binding.wvGithub.settings.javaScriptEnabled = true
        binding.wvGithub.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Log.d(TAG,url)
                if (url!!.startsWith("https://xdaysofcode.firebaseapp.com")){
                    var code = Uri.parse(url).getQueryParameter("code")
                    getAccessToken(code)
                    Log.d(TAG,code)
                    return true
                }

                return false
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

    private fun getAccessToken(code: String) {
        Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build().create(GithubApi::class.java).getAccessToken(AccessTokenBody(getString(R.string.client_id),
                getString(R.string.client_secret),code,getString(R.string.redirect_uri)))
                .enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        val splitted = response!!.body()!!.string()
                        Log.d(TAG,splitted)
                        val b = splitted.split("[=&]".toRegex())[1]
                        Log.d(TAG,"token is ${b}")
                        firebaseAuthWithToken(b)
                    }

                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Log.e(TAG,t.toString())
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
        clearDataBeforeLaunch()
        binding.wvGithub.visibility = View.VISIBLE
        val httpUrl = HttpUrl.Builder()
                .scheme("https")
                .host("github.com")
                .addPathSegment("login")
                .addPathSegment("oauth")
                .addPathSegment("authorize")
                .addQueryParameter("client_id",getString(R.string.client_id))
                .addQueryParameter("scope","read:user")
                .build()
        binding.btnGithubAuth.visibility = View.GONE

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

    private fun clearDataBeforeLaunch() {
        val cookieManager = CookieManager.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(ValueCallback<Boolean> { aBoolean ->
                // a callback which is executed when the cookies have been removed
                Log.d(TAG, "Cookie removed: " + aBoolean!!)
            })
        } else {

            cookieManager.removeAllCookie()
        }
    }
}

