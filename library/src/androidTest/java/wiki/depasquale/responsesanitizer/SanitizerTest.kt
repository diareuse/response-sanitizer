package wiki.depasquale.responsesanitizer

import android.util.Log
//import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

private val json = """
{
	"justField": "hasValue",
	"justFieldNumber": 0,
	"justFieldNumberPositive": 1,
	"justFieldNumberNegative": -1,
	"nullField": null,
	"nullField2": "null",
	"arrayField": [
		null,
		"null",
		"valid",
		0,
		1, -1,
		{
			"justField": "hasValue",
			"justFieldNumber": 0,
			"justFieldNumberPositive": 1,
			"justFieldNumberNegative": -1,
			"nullField": null,
			"nullField2": "null"
		}
	],
	"arrayObjectField": [{
		"justField": "hasValue",
		"justFieldNumber": 0,
		"justFieldNumberPositive": 1,
		"justFieldNumberNegative": -1,
		"nullField": null,
		"nullField2": "null"
	}, null, {
		"justField": "hasValue",
		"justFieldNumber": 0,
		"justFieldNumberPositive": 1,
		"justFieldNumberNegative": -1,
		"nullField": null,
		"nullField2": "null"
	}]
}
    """.trimIndent()

@RunWith(AndroidJUnit4::class)
class SanitizerTest {

    @Test
    fun test_TopLevel() {
        val result = Sanitizer { json.removeNulls() }
        Log.d("result", result)
        assert(result.contains("null"))
    }

}