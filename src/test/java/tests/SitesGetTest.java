package tests;

import endpoints.SitesGetEndpoint;
import io.qameta.allure.Step;
import lib.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static lib.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

@Epic("Tests for 'sites' endpoint")
@Feature("Method 'GET'")
public class SitesGetTest extends BaseTest {

    public static int[]ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    static Stream<Integer> provideIds() {
        return Arrays.stream(ids).boxed();
    }

    private SitesGetEndpoint sitesGetEndpoint;

    @BeforeEach
    @Step("Setting up the test")
    public void setUp() {
        super.setUp();
        sitesGetEndpoint = getEndpointInstance(SitesGetEndpoint.class);
    }

    // Using REST-assured assertions

    @Test
    @Description("This test checks if response body is JSON")
    @DisplayName("Positive test if JSON is response body")
    public void testSitesResponseJson(){
        sitesGetEndpoint.checkSitesResponseJson();

    }

    @Description("This test checks response status")
    @DisplayName("Positive test for status code and status line")
    @Test
    public void testSitesResponseStatus(){
        sitesGetEndpoint.checkSitesResponseStatus();

    }

    @Description("This test checks the size of response JSON")
    @DisplayName("Positive test for size of the response JSON")
    @Test
    public void testSitesResponseSize(){
        sitesGetEndpoint.checkSitesResponseSize(ids.length);

    }

    @Description("This test checks if response has correct status code")
    @DisplayName("Negative test for expected status code")
    @Test
    public void testWrongPathStatusCode(){
        sitesGetEndpoint.checkWrongPathStatusCode();

    }

    @Description("This test checks that response time doesn't exceed an expected time")
    @DisplayName("Positive test for response time")
    @Test
    public void testPerformanceSitesResponse(){
        sitesGetEndpoint.checkPerformanceSitesResponse(4000);
    }

    @Description("This test checks if response JSON has correct values")
    @DisplayName("Positive test for correct response JSON values")
    @Test
    public void testHeadersPresenceNotNullValueSitesResponse(){
        sitesGetEndpoint.checkHeadersPresenceNotNullValueSitesResponse();

    }

    @Description("This test checks if response JSON has required fields")
    @DisplayName("Positive test for correct response JSON fields")
    @ParameterizedTest
    @MethodSource("provideIds")
    public void testJsonResponseRequiredFields(int id) {
        sitesGetEndpoint.checkJsonResponseRequiredFields(id);

    }

    // Using JUnit assertions
    @Description("This test checks if response JSON has correct values")
    @DisplayName("Positive test for correct response JSON values")
    @Test
    public void testCorrectResponseValues(){
        Response response = sitesGetEndpoint.makeSitesGetRequest("/sites");

        assertEquals( 200, response.getStatusCode(), "Unexpected status code");
        assertPresenceValuesInResponse(response,"id",1);
        assertPresenceValuesInResponse(response,"name","mysite1");
        assertResponseJsonValueIsUnique(response, "id");
    }

    @Description("This test checks if response JSON has unique values")
    @DisplayName("Positive test if 'id' and 'name' has unique values")
    @Test
    public void testResponseUniqueValues(){
        Response response = sitesGetEndpoint.makeSitesGetRequest("/sites");

        assertResponseJsonValueIsUnique(response, "id");
        assertResponseJsonValueIsUnique(response, "name");
    }

    @Description("This test checks if response JSON has correct values")
    @DisplayName("Positive test for correct response JSON values")
    @Test
    public void testIdAndNameValuesJsonResponse(){
        Response response = sitesGetEndpoint.makeSitesGetRequest("/sites");

        assertPresenceValuesInResponse(response,"id", ids);
        assertPresenceValuesInResponse(response,"name", getAllSitesNames(ids));

    }

    @Description("This test checks response headers")
    @DisplayName("Positive test for correct response headers")
    @Test
    public void testResponseHeaders(){
        Response response = sitesGetEndpoint.makeSitesGetRequest("/sites");

        assertResponseHasHeaders(response,"Server");
        assertResponseHasHeaders(response,"Date");
        assertResponseHasHeaders(response,"Content-Type");
        assertResponseHasHeaders(response,"Content-Length");
        assertResponseHasHeaders(response,"Connection");

    }

    @Description("This test checks if solar capacity in response JSON has correct values")
    @DisplayName("Positive test for correct solar capacity values")
    @ParameterizedTest
    //@ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @MethodSource("provideIds")
    public void testSolarCapacityJsonResponse(int id){
        Response response = sitesGetEndpoint.makeGetRequestWithPathAndId("/sites",id);

        assertJsonResponseHasKeyWithValue(response, "solar_capacity");
        assertResponseStatusCode(response,200);
    }

    @Description("This test checks if response JSON has required fields")
    @DisplayName("Positive test for correct response JSON fields")
    @ParameterizedTest
    @MethodSource("provideIds")
    public void testSiteJsonFields(int id) {
        Response response = sitesGetEndpoint.makeGetRequestWithPathAndId("/sites",id);

        assertJsonResponseHasKey(response,"id");
        assertJsonResponseHasKey(response,"name");
        assertJsonResponseHasKey(response,"solar_capacity");
    }
}
