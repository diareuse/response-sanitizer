package wiki.depasquale.responsesanitizer

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

class ResponseSanitizer : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.header(CONTENT_TYPE, "") != CONTENT_TYPE_JSON) {
            return response
        }

        return response.edit {
            val body = Sanitizer { it.body()?.string()?.removeNulls().orEmpty() }
            val responseBody = ResponseBody.create(MediaType.parse(CONTENT_TYPE_JSON), body)
            body(responseBody)
        }
    }

    private fun Response.edit(builder: Response.Builder.(Response) -> Unit): Response = newBuilder()
        .apply { builder(this@edit) }
        .build()

    companion object {
        private const val CONTENT_TYPE = "Content-Type"
        private const val CONTENT_TYPE_JSON = "application/json"
    }

}
