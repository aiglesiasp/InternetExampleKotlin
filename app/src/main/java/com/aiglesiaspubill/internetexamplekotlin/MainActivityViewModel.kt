package com.aiglesiaspubill.internetexamplekotlin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivityViewModel : ViewModel() {

    val token = "eyJraWQiOiJwcml2YXRlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJpZGVudGlmeSI6IkM3QTZBRENFLUM3MjUtNDlFRi04MEFDLTMxNDVCODkxQzg5NCIsImV4cGlyYXRpb24iOjY0MDkyMjExMjAwLCJlbWFpbCI6ImFpZ2xlc2lhc3B1YmlsbEBnbWFpbC5jb20ifQ.NjSKR-UPBTVSNIKunr8QPjwUiZJcnUObOv0pYG28Avc"
    val stateLiveData: MutableLiveData<MainActivityState> by lazy {
        MutableLiveData<MainActivityState>()
    }



    //LLAMADA AL BOOTCAMP
    fun getBootcamps() {
        setValueOnMainThread(MainActivityState.Loading)
        val client = OkHttpClient()
        val url = "https://dragonball.keepcoding.education/api/data/bootcamps"
        val request = Request.Builder().url(url).build()
        val call = client.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(MainActivityViewModel::javaClass.name, "Error")
                setValueOnMainThread(MainActivityState.Error(e.message.toString()))
            }
            override fun onResponse(call: Call, response: Response) {
                Log.d(MainActivityViewModel::javaClass.name, "Success")
                val responseBody = response.body?.string()
                println(responseBody)
                val bootcampDtoArray: Array<BootcampDto> = Gson().fromJson(responseBody, Array<BootcampDto>::class.java)
                var contador =0
                var bootcampArray =  bootcampDtoArray.map {
                    Bootcamp(it.name, it.id, contador++)
                }
                println(bootcampArray)
                setValueOnMainThread(MainActivityState.SuccessBootcampList(bootcampArray))

            }
        })

    }

    //LLAMADA A API REST LOGIN
    fun apiCallToken() {
        setValueOnMainThread(MainActivityState.Loading)
        val client = OkHttpClient()
        val url = "https://dragonball.keepcoding.education/api/jwt/test"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()

        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                setValueOnMainThread(MainActivityState.Error(e.message.toString()))
            }

            override fun onResponse(call: Call, response: Response) {

                setValueOnMainThread(MainActivityState.SuccessJwtTest(response.body?.string()))
            }
        })
    }


    fun setValueOnMainThread(value: MainActivityState) {
        viewModelScope.launch(Dispatchers.Main) {
            stateLiveData.value = value
        }
    }

    sealed class MainActivityState {
        data class SuccessJwtTest (val token: String?) : MainActivityState()
        data class SuccessBootcampList (val bootcampList: List<Bootcamp>) : MainActivityState()
        data class Error (val message: String) : MainActivityState()
        object Loading : MainActivityState()
    }


}