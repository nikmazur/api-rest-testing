import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import springrest.Employee;

import java.util.HashMap;

import static io.restassured.http.ContentType.JSON;
import static org.testng.Assert.*;

public class APITests extends Methods {

    /* Smoke test for  server availability. Checks for the HTTP 200 status code.
     * All subsequent tests are dependent on this one (dependsOnMethods argument).
     * Will sometimes fail because of the random bool in Data. */
    @Test
    public void ping() {
        try {
            //Form HTTP request, check response status code. Comment this to disable random failing.
            assertEquals(Methods.getStatus(), 200);
        }
        catch(AssertionError ae) {
            //Example of a custom message upon test failure using try-catch blocks
            Assert.fail("Ping Failed! Resource was not available.");
        }
    }

    //Verify that the starting Employees set is not empty.
    @Test (dependsOnMethods = "ping")
    public void notEmpty() {
        assertFalse(getEmployees().isEmpty());
    }

    //Check the contents of JSON for specific data (in this case the name of employee by their index).
    //This test uses a data provider 'getEmpl' (listed at end of the class) and will run 2 times.
    @Test (dataProvider = "getEmpl", dependsOnMethods = "ping")
    public void checkEmployee(final int ID, final String NAME) {
        assertNotEquals(getEmployees().stream()
                .filter(x -> x.getId() == ID && x.getName().equals(NAME))
                .count(), 0);
    }

    //Check the functionality of adding a new employee.
    @Test (dependsOnMethods = "ping")
    public void addNewEmployee() {
        Employee newEmpl = new Employee
                (RandomUtils.nextInt(0, 100000), faker.name().fullName(), faker.company().profession(), RandomUtils.nextInt(18, 100));
        assert(addEmployee(newEmpl).stream().anyMatch(x -> x.equals(newEmpl)));
    }

    /* Try to remove an employee by a numeric index number.
     * This and the next test are set to run only after the other tests which are dependent on content
     * have been passed, so as not to interfere with them and cause a false fail. */
    @Test (dependsOnMethods = {"ping", "notEmpty", "checkEmployee"})
    public void delEmployeeIndex() {
        assertEquals(delEmployee("ind", 1), "Employee deleted");
    }

    //Try to remove an employee by their name
    @Test (dependsOnMethods = {"ping", "notEmpty", "checkEmployee"})
    public void delEmployeeName() {
        assertEquals(delEmployee("name", "Mary Jones"), "Employee deleted");
    }

    //Add a new employee with the salary separated by a comma (sent as a String).
    //Comma will be removed and the employee will be added successfully.
    @Test (dependsOnMethods = "ping")
    public void numComma() {


        HashMap<String, Object> bod = new HashMap<>();
        bod.put("name", "Sue Jackson");
        bod.put("age", RandomUtils.nextInt(1, 100));
        //Generate random salary separated by ','
        bod.put("salary", RandomStringUtils.randomNumeric(2) + "," + RandomStringUtils.randomNumeric(3));

        RestAssured.given().
                contentType(JSON).
                body(bod).
                when().
                post("/employees/add").
                then().
                assertThat().statusCode(200);
    }

    //Negative tests.
    //Try to access a bad URL (part of which is randomly generated).
    @Test (dependsOnMethods = "ping")
    public void negBadURL() {
        //Generate a string of random characters
        final String RURL = RandomStringUtils.randomAlphabetic(5);

        RequestSpecification httpRequest = RestAssured.given();
        //Add the random string to base URL
        Response resp = httpRequest.get("http://localhost:8188/" + RURL);
        //Verify we get error 404 (Not Found) in return
        Assert.assertEquals(resp.getStatusCode(), 404);
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

    //Try to access delete method without any parameters. Expecting "Not found" message in return.
    @Test (dependsOnMethods = "ping")
    public void negDelNoParams() {
        String resp = (RestAssured.
                given().
                when().
                post("/employees/delete").
                then().
                assertThat().statusCode(200)).extract().response().asString();

        assertEquals(resp, "Employee not found");
    }

    //Try to pass wrong parameters - index instead of name. Expecting "Not found" in return.
    @Test (dependsOnMethods = "ping")
    public void negDelWrongArg() {
        String resp = (RestAssured.
                given().
                queryParam("name", "0").
                when().
                post("/employees/delete").
                then().
                assertThat().statusCode(200)).extract().response().asString();

        assertEquals(resp, "Employee not found");
    }

    /* Bad input. Adding an employee without a required field (name is missing).
     * In this test we're also expecting an exception by param 'expectedExceptions'
     * (Request will return status code 400 instead of 200, which will trigger an Assertion exception).*/
    @Test (expectedExceptions = AssertionError.class, dependsOnMethods = "ping")
    public void negAddEmpl() {
        HashMap<String, Object> bod = new HashMap<>();
        bod.put("age", RandomUtils.nextInt(1, 100));
        bod.put("salary", RandomUtils.nextInt(10000, 100000));

        RestAssured.given().
                contentType(JSON).
                body(bod).
                when().
                post("/employees/add").
                then().
                assertThat().statusCode(200);
    }

    //Bad input. Field salary contains letters instead of numbers.
    @Test (dependsOnMethods = "ping")
    public void negTextInput() {
        HashMap<String, Object> bod = new HashMap<>();
        bod.put("name", "Anne Clark");
        bod.put("age", RandomUtils.nextInt(1, 100));
        bod.put("salary", "A lot");

        RestAssured.given().
                contentType(JSON).
                body(bod).
                when().
                post("/employees/add").
                then().
                assertThat().statusCode(400);
    }

    //Bad input. Name does not contain any letters, only spaces.
    @Test (dependsOnMethods = "ping")
    public void negTextSpaces() {
        HashMap<String, Object> bod = new HashMap<>();
        bod.put("name", "   ");
        bod.put("age", RandomUtils.nextInt(1, 100));
        bod.put("salary", RandomUtils.nextInt(10000, 100000));

        RestAssured.given().
                contentType(JSON).
                body(bod).
                when().
                post("/employees/add").
                then().
                assertThat().statusCode(400);
    }



}
