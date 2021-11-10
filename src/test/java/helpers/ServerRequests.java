package helpers;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import models.Employee;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ServerRequests {

    private static RequestSpecification mainRequest() {
        return given().baseUri(RestAssured.baseURI)
                .contentType(ContentType.JSON).accept(ContentType.JSON).filter(new AllureFilter());
    }

    @Step("Get server status")
    public static int getStatus(String path) {
        return mainRequest().basePath(path).get().getStatusCode();
    }

    @Step("Retrieve all employees, return as list")
    public static List<Employee> getEmployees() {
        return Arrays.asList(mainRequest().basePath("/employees").get()
                .then().assertThat().statusCode(200).extract().as(Employee[].class));
    }

    @Step("Add new employee, return new list of employees")
    public static List<Employee> addEmployee(Employee empl, int statusCode) {
        return Arrays.asList(mainRequest().basePath("/employees").body(empl).put()
                .then().assertThat().statusCode(statusCode).extract().as(Employee[].class));
    }

    @Step("Request to delete an employee (by Name or Index)")
    public static List<Employee> delEmployee(String type, Object id, int statusCode) {
        return Arrays.asList(mainRequest().basePath("/employees").header(type, id).delete()
                .then().assertThat().statusCode(statusCode).extract().as(Employee[].class));
    }
}
