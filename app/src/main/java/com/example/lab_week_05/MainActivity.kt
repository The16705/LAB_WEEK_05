package com.example.lab_week_05

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lab_week_05.api.CatApiService
import com.example.lab_week_05.model.ImageData
import retrofit2.*
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var apiResponseView: TextView
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiResponseView = findViewById(R.id.api_response)
        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        val call = catApiService.searchImages(1, "full")
        call.enqueue(object : Callback<List<ImageData>> {
            override fun onResponse(
                call: Call<List<ImageData>>,
                response: Response<List<ImageData>>
            ) {
                if (response.isSuccessful) {
                    val images = response.body()
                    if (!images.isNullOrEmpty()) {
                        val firstImage = images[0]
                        apiResponseView.text = firstImage.imageUrl
                    } else {
                        apiResponseView.text = "No images found"
                    }
                } else {
                    Log.e(MAIN_ACTIVITY, "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(MAIN_ACTIVITY, "Network error", t)
            }
        })
    }

    companion object {
        private const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
    }
}
