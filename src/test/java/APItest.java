import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import springrest.Application;

import java.util.ArrayList;
import java.util.HashMap;

import io.restassured.specification.RequestSpecification;
import static io.restassured.http.ContentType.JSON;

public class APItest {

    //This method launches the Spring REST server in src.main before running any test
    @BeforeSuite
    public void launchServer() {
        Application.main(new String[]{""});
    }

    //A smoke test for  server availability. Checks for the HTTP 200 status code.
    //All subsequent tests are dependant on this one (dependsOnMethods argument).
    //Will fail randomly because of the random bool in Data.
    @Test
    public void ping() {
        try {
            RequestSpecification httpRequest = RestAssured.given();
            Response resp = httpRequest.get("http://localhost:8188/ping");
            //Checking the status code here. Comment this out to make the test always pass.
            Assert.assertEquals(200, resp.getStatusCode());
        }
        catch(AssertionError ae) {
            //Example of a custom message upon test fail using try-catch.
            Assert.fail("Ping Failed! Resource was not available.");
        }
    }

    @Test (dependsOnMethods = "ping")
    public void notEmpty() {
        RestAssured.baseURI = "http://localhost:8188";
        RestAssured.given().
                when().
                get("/employees").
                then().
                assertThat().statusCode(200).and().contentType(JSON).and().
                body("isEmpty()", Matchers.is(false));
    }

    @Test (dependsOnMethods = "ping")
    public void checkOneEmployee() {
        RestAssured.baseURI = "http://localhost:8188";
        RestAssured.given().
                when().
                get("/employees").
                then().
                assertThat().statusCode(200).and().contentType(JSON).and().
                body("0.name", Matchers.equalTo("Mary Jones"));
    }


    @Test (dependsOnMethods = "ping")
    public void addNewEmployee() {
        HashMap<String, String> bod = new HashMap<>();
        bod.put("name", "Anne Clark");
        bod.put("age", "32");
        bod.put("salary", "71790");

        RestAssured.baseURI = "http://localhost:8188";
        String resp = (RestAssured.
                given().
                queryParam("add", "").
                contentType(JSON).
                body(bod).
                when().
                post("/employees").
                then().
                assertThat().statusCode(200).and().contentType(JSON)).extract().response().asString();

        ArrayList<String> names = JsonPath.read(resp, "$..name");

        if(!names.contains("Anne Clark"))
            Assert.fail("Added name was not found!");
    }
}
