package endpoints;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class VppsGetEndpoint {

    private RequestSpecification requestSpec;

    public VppsGetEndpoint(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    @Step("Checks if response handles wrong path request")
    public void checkWrongVppsRequest(String path){
        Response response = makeGetRequestWithPath(path);

        response
                .then()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 NOT FOUND")
                .contentType(ContentType.fromContentType("text/html; charset=utf-8"));
    }

    @Step("Checks if ContentType is JSON")
    public void checkSitesResponseJson(String path){
        Response response = makeGetRequestWithPath(path);

        response
                .then()
                .contentType(ContentType.JSON);
    }

    public void checkVppsResponseStatus(int statusCode, String statusLine){
        Response response = makeGetRequestWithPath("/vpps");

        response
                .then()
                .statusCode(statusCode)
                .statusLine(statusLine);
    }

    @Step("Checks response status code and line")
    public void checkVppsResponseStatus(String path){
        Response response = makeGetRequestWithPath(path);

        response
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK");
    }

    @Step("Checks response size")
    public void checkVppsResponseSize(String path,int size){
        Response response = makeGetRequestWithPath(path);

        response
                .then()
                .body("size()", is(size));
    }

    @Step("Checks if response time less than expected")
    public void checkPerformanceSitesResponse(String path,long time){
        Response response = makeGetRequestWithPath(path);

        response
                .then()
                .time(lessThan(time));
    }

    @Step("Checks response headers")
    public void checkSitesResponseHeadersPresence(String path){
        Response response = makeGetRequestWithPath(path);

        response
                .then()
                .header("Server", "Werkzeug/2.3.4 Python/3.10.0")
                .header("Date", notNullValue())
                .header("Content-Type", "application/json")
                .header("Content-Length", notNullValue())
                .header("Connection", "close");

    }

    @Step("Checks required fields in the response")
    public void checkJsonResponseRequiredFields(String path,int id) {
        Response response = makeGetRequestWithPathAndId(path, id);
         response
                .then()
                .body("id",is(id))
                .body("name", is("vpp"+id))
                .body("total_capacity",greaterThanOrEqualTo(10000));

    }

    // Base Methods
    @Step("Makes get request with provided path")
    public Response makeGetRequestWithPath(String path) {
        return RestAssured
                .given()
                .spec(requestSpec)
                .basePath(path)
                .when()
                .get()
                .andReturn();
    }

    @Step("Makes get request with provided path and id")
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
