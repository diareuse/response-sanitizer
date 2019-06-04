package wiki.depasquale.responsesanitizer

import org.json.JSONArray
import org.json.JSONObject

object Sanitizer : SanitizerBacking() {

    inline operator fun <Out> invoke(block: Sanitizer.() -> Out) = this.block()

    fun JSONArray.removeNulls(): JSONArray {
        return filterNotNull()
    }

    fun JSONObject.removeNulls(): JSONObject {
        return filterNotNull()
    }

    fun String.removeNulls(): String {
        return jsonArray?.removeNulls()?.toString(0)
            ?: jsonObject?.removeNulls()?.toString(0)
            ?: this
    }

    fun String.removeLineBreaks() = replace("\n", "")
        .replace("\r", "")

}