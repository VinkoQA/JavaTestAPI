package lib;

import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {


    public static void assertJsonValueByName(Response response, String name, int expectedValue){
        response.then().assertThat().body("$", hasKey(name));

        int value = response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertResponseJsonValueIsUnique(Response response, String name){
        List<Integer> ids = response.jsonPath().getList(name);
        assertTrue(ids.stream().distinct().count() == ids.size());
    }

    public static void assertPresenceValuesInResponse(Response response, String name, String[] expectedValues){
        for (String expectedValue : expectedValues) {
            assertTrue(hasValueInList(response, name, expectedValue), "There is no value for "+name);
        }
    }

    public static void assertPresenceValuesInResponse(Response response, String name, int[] expectedValues){
        for (int expectedValue : expectedValues) {
            assertTrue(hasValueInList(response, name, expectedValue), "There is no value for "+name);
        }
    }


    public static void assertPresenceValuesInResponse(Response response, String name, int expectedValue){
        assertTrue(hasValueInList(response, name, expectedValue), "There is no value for "+name);
    }

    public static void assertPresenceValuesInResponse(Response response, String name, String expectedValue){
        assertTrue(hasValueInList(response, name, expectedValue), "There is no value for "+name);
    }

    public static void assertPresenceValueInResponse(Response response, String name, String expectedValue){
        assertTrue(hasValue(response, name, expectedValue), "There is no value for "+name);
    }

    public static void assertPresenceValueInResponse(Response response, String name, int expectedValue){
        assertTrue(hasValue(response, name, expectedValue), "There is no value for "+name);
    }

    public static boolean hasValue(Response response, String name, int expectedValue) {
        JsonPath jsonPath = JsonPath.from(response.getBody().asString());
        int actualValue = jsonPath.getInt(name);
        return actualValue == expectedValue;
    }

    public static boolean hasValue(Response response, String name, String expectedValue) {
        JsonPath jsonPath = JsonPath.from(response.getBody().asString());
        String actualValue = jsonPath.getString(name);
        return actualValue.equals(expectedValue);
    }



    public static boolean hasValueInList(Response response, String name, int expectedValue) {
        JsonPath jsonPath = JsonPath.from(response.getBody().asString());
        List<Integer> list = jsonPath.getList(name);

        return list.contains(expectedValue);
    }

    public static boolean hasValueInList(Response response, String name, String expectedValue) {
        JsonPath jsonPath = JsonPath.from(response.getBody().asString());
        List<Integer> list = jsonPath.getList(name);

        return list.contains(expectedValue);
    }

    public static void assertResponseTextAnswer(Response response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                response.asString(),
                "Response text is not as expected"
        );

    }

    public static void assertResponseStatusCode(Response response, int expectedStatusCode){
        assertEquals(
                expectedStatusCode,
                response.statusCode(),
                "Response code is not as expected"
        );

    }

    public static void assertResponseStatusLine(Response response, String expectedStatusLine){
        assertEquals(
                expectedStatusLine,
                response.statusLine(),
                "Response status line is not as expected"
        );

    }

    public static void assertJsonResponseHasKey(Response response, String expectedKey){
        response.then().assertThat().body("$",hasKey(expectedKey));
    }

    public static void assertJsonResponseHasKeyWithValue(Response response, String expectedKey){
        response.then().assertThat().body(expectedKey, Matchers.notNullValue());
    }

    public static void assertResponseHasHeaders(Response response, String headerName){
        Headers headers = response.getHeaders();
        assertTrue(headers.hasHeaderWithName(headerName));
    }

}
