package endpoints;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.*;

public class SitesGetEndpoint {

    private RequestSpecification requestSpec;

    public SitesGetEndpoint(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    public RequestSpecification getRequestSpec() {
        return requestSpec;
    }

    @Step("Checks if ContentType is JSON")
    public void checkSitesResponseJson(){
        Response response = makeSitesGetRequest("/sites");
        response
                .then()
                .contentType(ContentType.JSON);

    }

    @Step("Checks if statusCode is 200 and statusLine is 'HTTP/1.1 200 OK'")
    public void checkSitesResponseStatus(){
        Response response = makeSitesGetRequest("/sites");
        response
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK");

    }

    @Step("Checks the response JSON for expected size")
    public void checkSitesResponseSize(int size){
        Response response = makeSitesGetRequest("/sites");
        response
                .then()
                .body("size()", is(size));

    }

    @Step("Checks if statusCode is 404 and statusLine is 'HTTP/1.1 404 NOT FOUND'")
    public void checkWrongPathStatusCode(){
        Response response = makeSitesGetRequest("/sitess");
        response
                .then()
                .statusLine("HTTP/1.1 404 NOT FOUND")
                .statusCode(404);
    }

    @Step("Checks if response time less than expected value")
    public void checkPerformanceSitesResponse(long time){
        Response response = makeSitesGetRequest("/sites");
        response
                .then()
                .time(lessThan(time));
    }

    @Step("Checks if response has expected headers")
    public void checkHeadersPresenceNotNullValueSitesResponse(){
        Response response = makeSitesGetRequest("/sites");
        response
                .then()
                .header("Server", "Werkzeug/2.3.4 Python/3.10.0")
                .header("Date", notNullValue())
                .header("Content-Type", "application/json")
                .header("Content-Length", notNullValue())
                .header("Connection", "close");

    }

    @Step("Checks if response body has expected fields and values")
    public void checkJsonResponseRequiredFields(int id) {
        Response  response = makeGetRequestWithPathAndId("/sites",id);
        response
                .then()
                .body("id",is(id))
                .body("name", is("mysite"+id))
                .body("solar_capacity",is(10000));

    }

    // Base Methods
    @Step("Makes GET request for all sites")
    public Response makeSitesGetRequest(String path) {
        return RestAssured
                .given()
                .spec(requestSpec)
                .basePath(path)
                .when()
                .get()
                .andReturn();
    }

    @Step("Makes GET request for the specific site")
    public Response makeGetRequestWithPathAndId(String path, int id) {
        return RestAssured
                .given()
                .spec(requestSpec)
                .basePath(path)
                .when()
                .get("/" +id)
                .andReturn();
    }
}
