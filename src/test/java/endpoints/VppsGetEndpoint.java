package endpoints;

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

    public void checkWrongVppsRequest(String path){
        Response response = makeGetRequestWithPath(path);

        response
                .then()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 NOT FOUND")
                .contentType(ContentType.fromContentType("text/html; charset=utf-8"));
    }

    public void checkSitesResponseJson(){
        Response response = makeGetRequestWithPath("/vpps");

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

    public void checkVppsResponseStatus(){
        Response response = makeGetRequestWithPath("/vpps");

        response
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK");
    }

    public void checkVppsResponseSize(int size){
        Response response = makeGetRequestWithPath("/vpps");

        response
                .then()
                .body("size()", is(size));
    }

    public void checkPerformanceSitesResponse(){
        Response response = makeGetRequestWithPath("/vpps");

        response
                .then()
                .time(lessThan(4000L));
    }

    public void checkSitesResponseHeadersPresence(){
        Response response = makeGetRequestWithPath("/vpps");

        response
                .then()
                .header("Server", "Werkzeug/2.3.4 Python/3.10.0")
                .header("Date", notNullValue())
                .header("Content-Type", "application/json")
                .header("Content-Length", notNullValue())
                .header("Connection", "close");

    }

    public void checkJsonResponseRequiredFields(int id) {
        Response response = makeGetRequestWithPathAndId("/vpps", id);
         response
                .then()
                .body("id",is(id))
                .body("name", is("vpp"+id))
                .body("total_capacity",greaterThanOrEqualTo(10000));

    }

    // Base Methods
    public Response makeGetRequestWithPath(String path) {
        return RestAssured
                .given()
                .spec(requestSpec)
                .basePath(path)
                .when()
                .get()
                .andReturn();
    }

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
