package wiki.depasquale.responsesanitizer

import org.json.JSONArray
import org.json.JSONObject

abstract class SanitizerBacking {

    private val _NULL = "null"
    private val _NIL = "nil"

    val String.jsonArray get() = runCatching { JSONArray(this) }.getOrNull()
    val String.jsonObject get() = runCatching { JSONObject(this) }.getOrNull()

    protected fun JSONArray.filterNotNull(): JSONArray {
        val newArray = JSONArray()
        for (index in 0 until length()) {
            val value = tryArray(index)?.filterNotNull()
                ?: tryObject(index)?.filterNotNull()
                ?: get(index)
            newArray.putSafe(value)
        }
        return newArray
    }

    protected fun JSONObject.filterNotNull(): JSONObject {
        val newObject = JSONObject()
        val keys = keys().asSequence().toList()
        for (index in 0 until keys.size) {
            val key = keys[index]
            val value = tryArray(key)?.filterNotNull()
                ?: tryObject(key)?.filterNotNull()
                ?: get(key)
            newObject.putSafe(key, value)
        }
        return newObject
    }

    private fun JSONArray.tryObject(index: Int): JSONObject? =
        runCatching { getJSONObject(index) }.getOrNull()

    private fun JSONArray.tryArray(index: Int): JSONArray? =
        runCatching { getJSONArray(index) }.getOrNull()

    private fun JSONObject.tryObject(name: String): JSONObject? =
        runCatching { getJSONObject(name) }.getOrNull()

    private fun JSONObject.tryArray(name: String): JSONArray? =
        runCatching { getJSONArray(name) }.getOrNull()

    private fun JSONArray.putSafe(obj: Any?) {
        if (obj.isActuallyNull()) {
            return
        }
        put(obj)
    }

    private fun JSONObject.putSafe(name: String?, obj: Any?) {
        if (obj.isActuallyNull() || name.isActuallyNull()) {
            return
        }
        put(name, obj)
    }

    private fun Any?.isActuallyNull(): Boolean {
        return this == null || this == JSONObject.NULL || this == _NULL || this == _NIL
    }

}