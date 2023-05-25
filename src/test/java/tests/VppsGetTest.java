package tests;

import endpoints.VppsGetEndpoint;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lib.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@Epic("Tests 'sites/vpps' endpoint")
@Feature("Method 'GET'")
public class VppsGetTest extends BaseTest {

    String path = "/vpps";

    public static int[]ids = {1, 2};

    static Stream<Integer> provideIds() {
        return Arrays.stream(ids).boxed();
    }

    private VppsGetEndpoint vppsGetEndpoint;

    @BeforeEach
    @Step("Setting up the test")
    public void setUp() {
        super.setUp();
        vppsGetEndpoint = getEndpointInstance(VppsGetEndpoint.class);
    }

    //    Using REST Assured assertions
    //    Negative scenarios:
    //  Check the response for correct handling a request with wrong vpps
    @Description("This test checks a wrong path response")
    @DisplayName("Negative test for a wrong path")
    @Test
    public void testMakeWrongVppsRequest(){
        String path = "/vpps/";
        vppsGetEndpoint.checkWrongVppsRequest(path);

    }

    @Description("This test checks a wrong path response")
    @DisplayName("Negative test for a wrong path")
    @Test
    public void testWrongPathStatusCode(){
        String path = "/vppss";
        vppsGetEndpoint.checkWrongVppsRequest(path);
    }

    //    Positive scenarios:
    //  Check the response content type is json
    @Description("This test checks a response type")
    @DisplayName("Positive test to check response body is Json")
    @Test
    public void testSitesResponseIsJson(){
        vppsGetEndpoint.checkSitesResponseJson(path);

    }

    //  Check status code and status line of the response
    @Description("This test checks status code and status line of the response")
    @DisplayName("Positive test to check status code and status line of the response")
    @Test
    public void testVppsResponseSuccessStatus(){
        vppsGetEndpoint.checkVppsResponseStatus(path);

    }

    //  Check how many vpps in the response
    @Description("This test checks how many vpps in the response body")
    @DisplayName("Positive test to check the amount of vpps in the response")
    @Test
    public void testVppsResponseSize(){
        int size = ids.length;
        vppsGetEndpoint.checkVppsResponseSize(path,size);

    }

    //  Check performance of the response
    @Description("This test checks the performance of the response")
    @DisplayName("Positive test to check the performance of the response")
    @Test
    public void testPerformanceSitesResponse(){
        vppsGetEndpoint.checkPerformanceSitesResponse(path,4000);
    }

    //  Check headers of the response
    @Description("This test checks headers of the response")
    @DisplayName("Positive test to check headers of the response")
    @Test
    public void testHeadersPresenceNotNullValueSitesResponse(){
        vppsGetEndpoint.checkSitesResponseHeadersPresence(path);

    }

    //  Check the response json contains required fields
    @Description("This test checks the response json contains required fields")
    @DisplayName("Positive test to check required fields of the response")
    @ParameterizedTest
    //@ValueSource(ints = {1, 2})
    @MethodSource("provideIds")
    public void testJsonResponseRequiredFields(int id) {
        vppsGetEndpoint.checkJsonResponseRequiredFields(path,id);

    }

    // Using JUnit assertions

    @Description("This test checks response values")
    @DisplayName("Positive test to check response values")
    @Test
    public void testCorrectResponseValues(){
        Response response = vppsGetEndpoint.makeGetRequestWithPath(path);

        assertEquals( 200, response.getStatusCode(), "Unexpected status code");
        assertPresenceValuesInResponse(response,"id",1);
        assertPresenceValuesInResponse(response,"name","vpp1");

    }

    @Description("This test checks if values are unique in the response")
    @DisplayName("Positive test to check uniqueness of the response values")
    @Test
    public void testResponseUniqueValues(){
        Response response = vppsGetEndpoint.makeGetRequestWithPath(path);

        assertResponseJsonValueIsUnique(response, "id");
        assertResponseJsonValueIsUnique(response, "name");
    }

    @Description("This test checks values of Id and Name in the response")
    @DisplayName("Positive test to check values of the response")
    @Test
    public void testIdAndNameValuesJsonResponse(){
        Response response = vppsGetEndpoint.makeGetRequestWithPath(path);

        assertPresenceValuesInResponse(response,"id", ids);
        assertPresenceValuesInResponse(response,"name", getAllVppsNames(ids));

    }

    @Description("This test checks headers of the response")
    @DisplayName("Positive test to check headers of the response")
    @Test
    public void testResponseHeaders(){
        Response response = vppsGetEndpoint.makeGetRequestWithPath(path);

        assertResponseHasHeaders(response,"Server");
        assertResponseHasHeaders(response,"Date");
        assertResponseHasHeaders(response,"Content-Type");
        assertResponseHasHeaders(response,"Content-Length");
        assertResponseHasHeaders(response,"Connection");

    }
    @Description("This test checks total capacity in the response")
    @DisplayName("Positive test to check total capacity in the response")
    @ParameterizedTest
    @MethodSource("provideIds")
    public void testTotalCapacityJsonResponse(int id){
        Response response = vppsGetEndpoint.makeGetRequestWithPathAndId(path,id);

        assertJsonResponseHasKeyWithValue(response,"total_capacity");
        assertResponseStatusCode(response,200);
    }

    @Description("This test checks Json fields in the response")
    @DisplayName("Positive test to check Json fields in the response")
    @ParameterizedTest
    @MethodSource("provideIds")
    public void testSiteJsonFields(int id) {
        Response response = vppsGetEndpoint.makeGetRequestWithPathAndId(path,id);

        assertJsonResponseHasKey(response,"id");
        assertJsonResponseHasKey(response,"name");
        assertJsonResponseHasKey(response,"total_capacity");
    }
}
