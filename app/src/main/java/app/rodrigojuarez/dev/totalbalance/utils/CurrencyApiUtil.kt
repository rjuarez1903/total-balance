package app.rodrigojuarez.dev.totalbalance.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class CurrencyApiUtil {
    private val client = OkHttpClient()

    fun fetchCurrencyRates(url: String, callback: (JSONObject?) -> Unit) {
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
                callback(null)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    callback(null)
                    return
                }

                val responseData = response.body?.string()
                val json = JSONObject(responseData ?: "")
                callback(json)
            }
        })
    }
}

