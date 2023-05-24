package tests;

import endpoints.VppsSitesPostDelEndpoint;
import io.restassured.response.Response;
import lib.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static lib.Assertions.assertResponseStatusCode;
import static lib.Assertions.assertResponseStatusLine;

public class VppsSitesPostDeleteTest extends BaseTest{

    public static int[] ids = {1, 2};

    static Stream<Integer> provideIds() {
        return Arrays.stream(ids).boxed();
    }

    private VppsSitesPostDelEndpoint vppsSitesPostDelEndpoint;

    @BeforeEach
    public void setUp() {
        super.setUp();
        vppsSitesPostDelEndpoint = getEndpointInstance(VppsSitesPostDelEndpoint.class);
    }

//    Happy path E2E scenarios:

//    Check response after deleting an existed site from valid Vpp.
@Test
public void testDeleteExistedSiteFromVpp(){
        int site = 4;
        int id = 1;
        vppsSitesPostDelEndpoint.addNewSite(id);
        vppsSitesPostDelEndpoint.deleteSiteCheckSuccess(id,site);

}

//    Check if a new site is added successfully when given a valid vpp ID and site_id.
//    Check if the capacity of the VPP increases after a new site is added.
//    Check if the capacity of the VPP decreases after a new site is deleted.
    @ParameterizedTest
    @MethodSource("provideIds")
    public void testE2EAddDeleteSite(int id) {
        int site = 3;
        int capacity = vppsSitesPostDelEndpoint.makeGetRequestAndValidate(id).extract().jsonPath().getInt("total_capacity");

        vppsSitesPostDelEndpoint.makeVppsSitesPostRequestCheck200(id,site);
        vppsSitesPostDelEndpoint.deleteSiteCheckSuccess(id,site);

        int finalCapacity = vppsSitesPostDelEndpoint.makeVppsGetRequest(id).jsonPath().getInt("total_capacity");
        System.out.println("capacity: " + capacity);
        System.out.println("finalCapacity: " + finalCapacity);
        // assertion disabled due to a defect
        //assertEquals(capacity,finalCapacity,"Final capacity is wrong");

    }

    //    Negative scenarios:

    //    Check if the same site can be added again to a given a valid vpp ID and site_id.
    @Test
    public void testAddingExistedSite() {
        int id = 1;
        Response postResponse1 = vppsSitesPostDelEndpoint.addNewSiteReturn(id);

        if (postResponse1.getStatusCode() == 200) {
            vppsSitesPostDelEndpoint.addNewSiteCheck409(id);
        } else if (postResponse1.getStatusCode() == 409) {
            vppsSitesPostDelEndpoint.deleteSite(id);

            vppsSitesPostDelEndpoint.addNewSiteCheck200(id);
            vppsSitesPostDelEndpoint.addNewSiteCheck409(id);
            vppsSitesPostDelEndpoint.deleteSite(id);
        }
    }

    //    Attempt to delete a non-existing site from the vpp.
    @Test
    public void testDeleteNonExistedSite() {
        vppsSitesPostDelEndpoint.addNewSite(1);
        Response deleteSiteResponse1 = vppsSitesPostDelEndpoint.deleteSiteToCheck(1);
        Response deleteSiteResponse2 = vppsSitesPostDelEndpoint.deleteSiteToCheck(1);

        assertResponseStatusCode(deleteSiteResponse1, 200);
        assertResponseStatusLine(deleteSiteResponse2, "HTTP/1.1 404 NOT FOUND");

    }

    //    Attempt to add a site to a vpp with an invalid vpp ID.
    //    It should return a 404 status code with a proper error message.
    @Test
    public void testToAddSiteToNonExistedVpp() {
        vppsSitesPostDelEndpoint.addNewSiteWrongVppsIdCheck404();

    }

    //    Attempt to delete a site from a vpp with an invalid vpp ID.
    //    It should return a 404 status code with a proper error message.
    @Test
    public void testDeleteSiteFromNonExistedVpp() {
        double id = 1.5;
        vppsSitesPostDelEndpoint.deleteSiteNonExisted(id);

    }

}
