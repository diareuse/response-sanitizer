package wiki.depasquale.responsesanitizer

import android.os.Build
import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ResponseSanitizer : Interceptor {

    private val lock = Any()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request).edit {
            val body = it.body()?.string()?.removeNulls().orEmpty()
            val responseBody = ResponseBody.create(MediaType.parse("application/json"), body)
            body(responseBody)
        }
    }

    private fun Response.edit(builder: Response.Builder.(Response) -> Unit): Response = newBuilder()
        .apply { builder(this@edit) }
        .build()

    private fun String.removeNulls(): String = try {
        JSONObject(this)
            .apply { removeNulls() }
            .toString()
    } catch (e: JSONException) {
        try {
            JSONArray(this)
                .apply { removeNulls() }
                .toString()
        } catch (e: JSONException) {
            Log.e(TAG, "", e)
            Log.e(TAG, this)
            Log.e(TAG, "This is neither an array nor an object; halting removing nulls.")
            this
        }
    }

    private fun JSONObject.removeNulls() {
        keys().forEach {
            if (isNull(it)) {
                synchronized(lock) { remove(it) }
            }
            if (isArray(it)) {
                getJSONArray(it).removeNulls()
            }
            if (isObject(it)) {
                getJSONObject(it).removeNulls()
            }
        }
    }

    private fun JSONArray.removeNulls() {
        (0 until length()).reversed().forEach {
            if (isNull(it)) {
                synchronized(lock) { removeCompat(it) }
            }
            if (isArray(it)) {
                getJSONArray(it).removeNulls()
            }
            if (isObject(it)) {
                getJSONObject(it).removeNulls()
            }
        }
    }

    private fun JSONObject.isArray(name: String): Boolean = try {
        getJSONArray(name)
        true
    } catch (e: JSONException) {
        false
    }

    private fun JSONArray.isArray(index: Int): Boolean = try {
        getJSONArray(index)
        true
    } catch (e: JSONException) {
        false
    }

    private fun JSONObject.isObject(name: String): Boolean = try {
        getJSONObject(name)
        true
    } catch (e: JSONException) {
        false
    }

    private fun JSONArray.isObject(index: Int): Boolean = try {
        getJSONObject(index)
        true
    } catch (e: JSONException) {
        false
    }

    private fun JSONArray.removeCompat(index: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            remove(index)
        } else {
            Log.e(
                TAG,
                "Removing values from JSONArray is not possible on " +
                        "API${Build.VERSION.SDK_INT}, raise minSdkVersion to " +
                        "API${Build.VERSION_CODES.KITKAT}"
            )
        }
    }

    companion object {
        private val TAG = ResponseSanitizer::class.java.simpleName
    }
}
