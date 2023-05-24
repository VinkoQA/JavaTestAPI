package tests;

import endpoints.VppsGetEndpoint;
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

public class VppsGetTest extends BaseTest {

    String path = "/vpps";

    public static int[]ids = {1, 2};

    static Stream<Integer> provideIds() {
        return Arrays.stream(ids).boxed();
    }

    private VppsGetEndpoint vppsGetEndpoint;

    @BeforeEach
    public void setUp() {
        super.setUp();
        vppsGetEndpoint = getEndpointInstance(VppsGetEndpoint.class);
    }

    //    Using REST Assured assertions
    //    Negative scenarios:
    //  Check the response for correct handling a request with wrong vpps
    @Test
    public void testMakeWrongVppsRequest(){
        String path = "/vpps/";
        vppsGetEndpoint.checkWrongVppsRequest(path);

    }

    @Test
    public void testWrongPathStatusCode(){
        String path = "/vppss";
        vppsGetEndpoint.checkWrongVppsRequest(path);
    }

    //    Positive scenarios:
    //  Check the response content type is json
    @Test
    public void testSitesResponseIsJson(){
        vppsGetEndpoint.checkSitesResponseJson();

    }

    //  Check status code and status line of the response
    @Test
    public void testVppsResponseSuccessStatus(){
        vppsGetEndpoint.checkVppsResponseStatus();

    }

    //  Check how many vpps in the response
    @Test
    public void testVppsResponseSize(){
        int size = ids.length;
        vppsGetEndpoint.checkVppsResponseSize(size);

    }

    //  Check performance of the response
    @Test
    public void testPerformanceSitesResponse(){
        vppsGetEndpoint.checkPerformanceSitesResponse();
    }

    //  Check headers of the response
    @Test
    public void testHeadersPresenceNotNullValueSitesResponse(){
        vppsGetEndpoint.checkSitesResponseHeadersPresence();

    }

    //  Check the response json contains required fields
    @ParameterizedTest
    //@ValueSource(ints = {1, 2})
    @MethodSource("provideIds")
    public void testJsonResponseRequiredFields(int id) {
        vppsGetEndpoint.checkJsonResponseRequiredFields(id);

    }

    // Using JUnit assertions

    @Test
    public void testCorrectResponseValues(){
        Response response = vppsGetEndpoint.makeGetRequestWithPath(path);

        assertEquals( 200, response.getStatusCode(), "Unexpected status code");
        assertPresenceValuesInResponse(response,"id",1);
        assertPresenceValuesInResponse(response,"name","vpp1");

    }

    @Test
    public void testResponseUniqueValues(){
        Response response = vppsGetEndpoint.makeGetRequestWithPath(path);

        assertResponseJsonValueIsUnique(response, "id");
        assertResponseJsonValueIsUnique(response, "name");
    }

    @Test
    public void testIdAndNameValuesJsonResponse(){
        Response response = vppsGetEndpoint.makeGetRequestWithPath(path);

        assertPresenceValuesInResponse(response,"id", ids);
        assertPresenceValuesInResponse(response,"name", getAllVppsNames(ids));

    }

    @Test
    public void testResponseHeaders(){
        Response response = vppsGetEndpoint.makeGetRequestWithPath(path);

        assertResponseHasHeaders(response,"Server");
        assertResponseHasHeaders(response,"Date");
        assertResponseHasHeaders(response,"Content-Type");
        assertResponseHasHeaders(response,"Content-Length");
        assertResponseHasHeaders(response,"Connection");

    }
    @ParameterizedTest
    @MethodSource("provideIds")
    public void testTotalCapacityJsonResponse(int id){
        Response response = vppsGetEndpoint.makeGetRequestWithPathAndId(path,id);

        assertJsonResponseHasKeyWithValue(response,"total_capacity");
        assertResponseStatusCode(response,200);
    }

    @ParameterizedTest
    @MethodSource("provideIds")
    public void testSiteJsonFields(int id) {
        Response response = vppsGetEndpoint.makeGetRequestWithPathAndId(path,id);

        assertJsonResponseHasKey(response,"id");
        assertJsonResponseHasKey(response,"name");
        assertJsonResponseHasKey(response,"total_capacity");
    }
}
