package endpoints;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.is;

public class VppsSitesPostDelEndpoint {

    private RequestSpecification requestSpec;

    public VppsSitesPostDelEndpoint(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    // GET methods
    public ValidatableResponse makeGetRequestAndValidate(int id) {
        Response response = makeGetRequest(id);

        return response
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .body("id", is(id))
                .body("name", is("vpp" + id))
                .contentType(ContentType.JSON);
    }

    public Response makeVppsGetRequest(int id) {
        return makeGetRequest(id);
    }

    // POST methods
    public void addNewSiteCheck409(int id) {
        Response response = sendPostRequest("/vpps/", id, "{\"site_id\": 4}", "application/json");
        validateResponseStatus(response, 409, "HTTP/1.1 409 CONFLICT");
    }

    public void makeVppsSitesPostRequestCheck200(int id, int site) {
        Response response = sendPostRequest("/vpps/", id, "{\"site_id\": " +site+"}", "application/json");
        validateResponseStatus(response, 200, "HTTP/1.1 200 OK");
        validateResponseBody(response, "success", is(true));
    }

    public void addNewSiteCheck200(int id) {
        Response response = sendPostRequest("/vpps/", id, "{\"site_id\": 4}", "application/json");
        validateResponseStatus(response, 200, "HTTP/1.1 200 OK");
        validateResponseBody(response, "success", is(true));
    }

    public void addNewSiteWrongVppsIdCheck404() {
        Response response = sendPostRequest("/vpps/", 1.5, "{\"site_id\": 4}", "application/json");
        validateResponseStatus(response, 404, "HTTP/1.1 404 NOT FOUND");
    }

    public void addNewSite(int id) {
        sendPostRequest("/vpps/", id,
                "{\"site_id\": 4}", "application/json");
    }

    public Response addNewSiteReturn(int id) {
        return sendPostRequest("/vpps/", id,
                "{\"site_id\": 4}", "application/json");
    }

    public void deleteSite(int id) {
        makeDeleteRequest(id, "{\"site_id\": 4}");
    }

    public void deleteSiteNonExisted(double id) {
        Response response = makeDeleteRequest(id, "{\"site_id\": 4}");

        response
                .then()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 NOT FOUND");
    }

    public void deleteSiteCheckSuccess(int id,int site) {
        Response response = makeDeleteRequest(id, "{\"site_id\": "+site+"}");

        response
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .body("success", is(true))
                .contentType(ContentType.JSON);
    }

    public Response deleteSiteToCheck(int id) {
        return makeDeleteRequest(id, "{\"site_id\": 4}");
    }

    // Base methods
    public Response makeGetRequest(int id) {
        return RestAssured
                .given()
                .spec(requestSpec)
                .basePath("/vpps/" + id)
                .when()
                .get()
                .andReturn();
    }

    public Response makeDeleteRequest(int id, String requestBody) {
        return RestAssured
                .given()
                .spec(requestSpec)
                .body(requestBody)
                .basePath("/vpps/" + id)
                .header("Content-Type", "application/json")
                .when()
                .delete("/sites")
                .andReturn();
    }

    public Response makeDeleteRequest(double id, String requestBody) {
        return RestAssured
                .given()
                .spec(requestSpec)
                .body(requestBody)
                .basePath("/vpps/" + id)
                .header("Content-Type", "application/json")
                .when()
                .delete("/sites")
                .andReturn();
    }
    protected Response sendPostRequest(String basePath, int id, String body, String contentType) {
        return RestAssured
                .given()
                .spec(requestSpec)
                .body(body)
                .basePath(basePath + id)
                .header("Content-Type", contentType)
                .when()
                .post("/sites");
    }

    protected Response sendPostRequest(String basePath, double id, String body, String contentType) {
        return RestAssured
                .given()
                .spec(requestSpec)
                .body(body)
                .basePath(basePath + id)
                .header("Content-Type", contentType)
                .when()
                .post("/sites");
    }

    protected void validateResponseStatus(Response response, int expectedStatusCode, String expectedStatusLine) {
        response.then()
                .statusCode(expectedStatusCode)
                .statusLine(expectedStatusLine);
    }
    
    protected void validateResponseBody(Response response, String jsonPath, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        response.then().body(jsonPath, matcher, additionalKeyMatcherPairs);
    }

}
