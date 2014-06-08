package uk.co.datumedge.hamcrest.json;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static uk.co.datumedge.hamcrest.json.JSONArrayComparatorFactory.jsonArrayComparison;
import static uk.co.datumedge.hamcrest.json.JSONAssertComparator.modalComparatorFor;
import static uk.co.datumedge.hamcrest.json.JSONObjectComparatorFactory.jsonObjectComparison;
import static uk.co.datumedge.hamcrest.json.StringComparatorFactory.stringComparison;

/**
 * Matcher that asserts that one JSON document is the same as another.
 *
 * @param <T>
 *            the type of the JSON document. This is typically {@code JSONObject}, {@code JSONArray} or {@code String}.
 */
public final class SameJSONAsCapturing<T> extends TypeSafeDiagnosingMatcher<T> {
	private final T expected;
	private final JSONModalComparator<T> comparator;
    private Map captured;

	public SameJSONAsCapturing(T expected, JSONModalComparator<T> comparator, Map captured) {
		this.expected = expected;
		this.comparator = comparator;
        this.captured = captured;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(expected.toString());
	}

	@Override
	protected boolean matchesSafely(T actual, Description mismatchDescription) {
		try {
			JSONComparisonResult result = comparator.compare(expected, actual);
			if (result.failed()) {
				mismatchDescription.appendDescriptionOf(result);
			}
			return result.passed();
		} catch (JSONException e) {
			StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			mismatchDescription.appendText(out.toString());
			return false;
		}
	}

	/**
	 * Creates a matcher that allows any element ordering within JSON arrays. For example,
	 * <code>{"fib":[0,1,1,2,3]}</code> will match <code>{"fib":[3,1,0,2,1]}</code>.
	 *
	 * @return the configured matcher
	 */
	public SameJSONAsCapturing<T> allowingAnyArrayOrdering() {
		return new SameJSONAsCapturing<T>(expected, comparator.butAllowingAnyArrayOrdering(), captured);
	}

	/**
	 * Creates a matcher that allows fields not present in the expected JSON document.  For example, if the expected
	 * document is
<pre>{
    "name" : "John Smith",
    "address" : {
        "street" : "29 Acacia Road"
    }
}</pre>
	 * then the following document will match:
<pre>{
    "name" : "John Smith",
    "age" : 34,
    "address" : {
        "street" : "29 Acacia Road",
        "city" : "Huddersfield"
    }
}</pre>
	 *
	 * All array elements must exist in both documents, so the expected document
<pre>[
    { "name" : "John Smith" }
]</pre>
	 *  will not match the actual document
<pre>[
    { "name" : "John Smith" },
    { "name" : "Bob Jones" }
]</pre>
	 *
	 * @return the configured matcher
	 */
	public SameJSONAsCapturing<T> allowingExtraUnexpectedFields() {
		return new SameJSONAsCapturing<T>(expected, comparator.butAllowingExtraUnexpectedFields(), new HashMap<String, String>());
	}

	/**
	 * Creates a matcher that compares {@code JSONObject}s.
	 *
	 * @param expected the expected {@code JSONObject} instance
	 * @return the {@code Matcher} instance
	 */
	@Factory
	public static SameJSONAsCapturing<JSONObject> sameJSONObjectAs(JSONObject expected) {
		return new SameJSONAsCapturing<JSONObject>(expected, modalComparatorFor(jsonObjectComparison()), new HashMap<String, String>());
	}

	@Factory
	public static SameJSONAsCapturing<JSONObject> sameJSONObjectAs(JSONObject expected, JSONModalComparator<JSONObject> comparator) {
		return new SameJSONAsCapturing<JSONObject>(expected, comparator, new HashMap<String, String>());
	}

	/**
	 * Creates a matcher that compares {@code JSONArray}s.
	 *
	 * @param expected the expected {@code JSONArray} instance
	 * @return the {@code Matcher} instance
	 */
	@Factory
	public static SameJSONAsCapturing<JSONArray> sameJSONArrayAs(JSONArray expected) {
		return new SameJSONAsCapturing<JSONArray>(expected, modalComparatorFor(jsonArrayComparison()), new HashMap<String, String>());
	}

	@Factory
	public static SameJSONAsCapturing<? super JSONArray> sameJSONArrayAs(JSONArray expected, JSONModalComparator<JSONArray> comparator) {
		return new SameJSONAsCapturing<JSONArray>(expected, comparator, new HashMap<String, String>());
	}

	/**
	 * Creates a matcher that compares {@code JSONObject}s or {@code JSONArray}s represented as {@code String}s.
	 *
	 * @param expected the expected JSON document
	 * @return the {@code Matcher} instance
	 */
	@Factory
	public static SameJSONAsCapturing<? super String> sameJSONAs(String expected) {
		return new SameJSONAsCapturing<String>(expected, modalComparatorFor(stringComparison()), new HashMap<String, String>());
	}
	
	@Factory
	public static SameJSONAsCapturing<? super String> sameJSONAs(String expected, JSONModalComparator<String> comparator) {
		return new SameJSONAsCapturing<String>(expected, comparator, new HashMap<String, String>());
	}
}
