package lib;

import endpoints.SitesGetEndpoint;
import endpoints.VppsGetEndpoint;
import endpoints.VppsSitesPostDelEndpoint;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTest {

    protected Map<Class<?>, Object> endpointInstances;

    @BeforeEach
    public void setUp() {
        endpointInstances = new HashMap<>();
        addEndpointInstance(VppsSitesPostDelEndpoint.class, createVppsSitesPostDelEndpoint());
        addEndpointInstance(VppsGetEndpoint.class, createVppsGetEndpoint());
        addEndpointInstance(SitesGetEndpoint.class, createSitesGetEndpoint());

    }

    private <T> void addEndpointInstance(Class<T> endpointClass, T endpointInstance) {
        endpointInstances.put(endpointClass, endpointInstance);
    }

    private SitesGetEndpoint createSitesGetEndpoint() {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://127.0.0.1")
                .setPort(5000)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        return new SitesGetEndpoint(requestSpec);
    }

    private VppsGetEndpoint createVppsGetEndpoint() {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://127.0.0.1")
                .setPort(5000)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        return new VppsGetEndpoint(requestSpec);
    }

    private VppsSitesPostDelEndpoint createVppsSitesPostDelEndpoint() {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://127.0.0.1")
                .setPort(5000)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        return new VppsSitesPostDelEndpoint(requestSpec);
    }

    protected <T> T getEndpointInstance(Class<T> endpointClass) {
        if (endpointInstances.containsKey(endpointClass)) {
            return endpointClass.cast(endpointInstances.get(endpointClass));
        }
        throw new IllegalArgumentException("Endpoint instance not found for class: " + endpointClass.getName());
    }

    protected String getHeader(Response response, String name){
        Headers headers = response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have header with name " + name);
        return headers.getValue(name);
    }

    protected int getIntValueFromJson(Response response, String name){
        response.then().assertThat().body(hasKey(name));
        return response.jsonPath().getInt(name);
    }

    protected String getStringValueFromJson(Response response, String name){
        response.then().assertThat().body(hasKey(name));
        return response.jsonPath().getString(name);
    }

    public String[] getAllVppsNames(int[] ids) {
        String[] sites = new String[ids.length];

        for (int i = 0; i < ids.length; i++) {
            sites[i] = "vpp" + ids[i];
        }
        return sites;
    }

        public String[] getAllSitesNames(int[] ids){
        String[] sites = new String[ids.length];

        for(int i = 0; i < ids.length; i++) {
            sites[i] = "mysite" + ids[i];
        }
        return sites;
    }
}
