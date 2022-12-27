package com.aiglesiaspubill.internetexamplekotlin

import android.util.Log
import androidx.lifecycle.ViewModel
import okhttp3.*
import java.io.IOException

class MainActivityViewModel : ViewModel() {

    //LLAMADA AL BOOTCAMP
    fun getBootcamps() {
        val client = OkHttpClient()
        val url = "https://dragonball.keepcoding.education/api/data/bootcamps"
        val request = Request.Builder().url(url).build()
        val call = client.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(MainActivityViewModel::javaClass.name, "Error")
            }
            override fun onResponse(call: Call, response: Response) {
                Log.d(MainActivityViewModel::javaClass.name, "Success")
                val responseBody = response.body?.string()
                println(responseBody)
            }
        })

    }


}