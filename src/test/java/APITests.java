import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import springrest.Application;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import io.restassured.specification.RequestSpecification;
import static io.restassured.http.ContentType.JSON;
import static org.testng.Assert.assertEquals;

public class APITests {

    //This method launches the Spring REST server in main before running any tests
    @BeforeSuite
    public void launchServer() {
        Application.main(new String[]{""});
        RestAssured.baseURI = "http://localhost:8188";
    }

    /*Smoke test for  server availability. Checks for the HTTP 200 status code.
    All subsequent tests are dependant on this one (dependsOnMethods argument).
    Will sometimes fail because of the random bool in Data.*/
    @Test
    public void ping() {
        try {
            RequestSpecification httpRequest = RestAssured.given();
            Response resp = httpRequest.get(RestAssured.baseURI + "/ping");
            //Checking the status code here. Comment this out to make the test always pass.
            Assert.assertEquals(200, resp.getStatusCode());
        }
        catch(AssertionError ae) {
            //Example of a custom message upon test failure using try-catch blocks.
            Assert.fail("Ping Failed! Resource was not available.");
        }
    }

    //Verify that the starting Employees set is not empty.
    //This and subsequent tests use the Gherkin synthax for Rest Assured requests.
    @Test (dependsOnMethods = "ping")
    public void notEmpty() {

        RestAssured.given().
                when().
                get("/employees").
                then().
                /* We're checking 3 things simultaneously:
                1. HTTP 200 code (OK)
                2. Response is in JSON format
                3. Body of the JSON response is not empty (isEmpty() == false) */
                assertThat().statusCode(200).and().contentType(JSON).and().
                body("isEmpty()", Matchers.is(false));
    }

    //Check the contents of JSON for specific data (in this case the name of the first employee)
    @Test (dependsOnMethods = "ping")
    public void checkOneEmployee() {

        RestAssured.given().
                when().
                get("/employees").
                then().
                assertThat().statusCode(200).and().contentType(JSON).and().
                body("0.name", Matchers.equalTo("Mary Jones"));
    }

    //Check the functionality of adding a new employee. Using POST instead of GET.
    @Test (dependsOnMethods = "ping")
    public void addNewEmployee() {
        //Forming the payload with the data about the new employee.
        HashMap<String, String> bod = new HashMap<>();
        bod.put("name", "Anne Clark");
        bod.put("age", "32");
        bod.put("salary", "71790");

        //Send the payload and extract the response, which is expected to have the new data.
        String resp = (RestAssured.given().
                contentType(JSON).
                body(bod).
                when().
                post("/employees/add").
                then().
                assertThat().statusCode(200).and().contentType(JSON)).extract().response().asString();

        //Extract the names of all employees from the response.
        ArrayList<String> names = JsonPath.read(resp, "$..name");

        //Checking for a match with the name we sent previously.
        //If the name is not in the list, then the addition did not work.
        if(!names.contains("Anne Clark"))
            Assert.fail("Added name was not found!");
    }

    /*Try to remove an employee by a numeric index number.
    This and the next test are set to run only after the other tests which are dependent on content
    have been passed, so as not to interfere with them and cause a false fail.*/
    @Test (dependsOnMethods = {"ping", "notEmpty", "checkOneEmployee"})
    public void delEmployeeIndex() {
        int index = 1;

        //The delete method is configured to return the result in a string format, which we can extract and verify
        String resp = (RestAssured.
                given().
                queryParam("ind", index).
                when().
                post("/employees/delete").
                then().
                assertThat().statusCode(200)).extract().response().asString();

        assertEquals("Employee deleted", resp);
    }

    //Try to remove an employee by their name.
    @Test (dependsOnMethods = {"ping", "notEmpty", "checkOneEmployee"})
    public void delEmployeeName() {
        String name = "Mary Jones";

        String resp = (RestAssured.
                given().
                queryParam("name", name).
                when().
                post("/employees/delete").
                then().
                assertThat().statusCode(200)).extract().response().asString();

        assertEquals("Employee deleted", resp);
    }

    //Negative tests start here.
    //Try to access a bad URL (part of which is randomly generated).
    @Test (dependsOnMethods = "ping")
    public void negBadURL() {
        //Generate an array of random characters and convert to string.
        byte[] array = new byte[5];
        new Random().nextBytes(array);
        String rURI = new String(array, Charset.forName("UTF-8"));

        RequestSpecification httpRequest = RestAssured.given();
        //Add the random string to base URI.
        Response resp = httpRequest.get("http://localhost:8188/" + rURI);
        //Verify we get error 404 (Not Found) in return.
        Assert.assertEquals(404, resp.getStatusCode());
    }

    //Bad input. Adding an employee without a required field (name is missing).
    @Test (dependsOnMethods = "ping")
    public void negAddEmpl() {
        HashMap<String, String> bod = new HashMap<>();
        bod.put("age", "32");
        bod.put("salary", "71790");

        RestAssured.given().
                contentType(JSON).
                body(bod).
                when().
                post("/employees/add").
                then().
                assertThat().statusCode(400);
    }

    //Bad input. Field salary contains letters instead of numbers.
    @Test (dependsOnMethods = "ping")
    public void negBadInput() {
        HashMap<String, String> bod = new HashMap<>();
        bod.put("name", "Anne Clark");
        bod.put("age", "32");
        bod.put("salary", "A lot");

        RestAssured.given().
                contentType(JSON).
                body(bod).
                when().
                post("/employees/add").
                then().
                assertThat().statusCode(400);
    }

    //Wrong HTTP method - GET instead of POST.
    @Test (dependsOnMethods = "ping")
    public void negGet() {
        RestAssured.given().
                queryParam("ind", "0").
                when().
                get("/employees/delete").
                then().
                assertThat().statusCode(405);
    }

    //Try to access delete methods without any parameters. Expecting "Not found" message in return.
    @Test (dependsOnMethods = "ping")
    public void negDelNoParams() {
        String resp = (RestAssured.
                given().
                when().
                post("/employees/delete").
                then().
                assertThat().statusCode(200)).extract().response().asString();

        assertEquals("Employee not found", resp);
    }

    //Try to pass wrong parameters - index as name. Expecting "Not found" in return.
    @Test (dependsOnMethods = "ping")
    public void negDelWrongArg() {
        String resp = (RestAssured.
                given().
                queryParam("name", "0").
                when().
                post("/employees/delete").
                then().
                assertThat().statusCode(200)).extract().response().asString();

        assertEquals("Employee not found", resp);
    }

}