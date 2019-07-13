import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import springrest.Application;
import springrest.Employee;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class Methods {

    public static Faker faker;

    @BeforeSuite
    @Step("This method launches the Spring REST server from main before running any tests")
    public void launchServer() {
        Application.main(new String[]{""});
        faker = new Faker();
        RestAssured.baseURI = "http://localhost:8188";
    }

    @Step("Main API Request Body")
    public static RequestSpecification mainRequest() {
        return given().baseUri(RestAssured.baseURI).contentType(ContentType.JSON).accept(ContentType.JSON);
    }

    @Step("Getting server status")
    public static int getStatus(String path) {
            return mainRequest().basePath(path).get().getStatusCode();
    }

    public static List<Employee> getEmployees() {
        return Arrays.asList(mainRequest().basePath("/employees").get().then().assertThat().statusCode(200).extract().as(Employee[].class));
    }

    public static List<Employee> addEmployee(Employee empl) {
        return Arrays.asList(mainRequest().basePath("/employees/add").body(empl).post()
                .then().assertThat().statusCode(200).extract().as(Employee[].class));
    }

    public static String delEmployee(String type, Object id) {
        return mainRequest().basePath("/employees/delete").queryParam(type, id).post()
                .then().assertThat().statusCode(200).extract().asString();
    }

    //Data provider used in the 'checkEmployee' test
    @DataProvider
    public Object[][] getEmpl()
    {
        //Create test array data object
        Object[][] data = new Object[2][2];

        //Add data about initial 2 employees (Index & Name)
        data[0][0] = 79400;
        data[0][1] = "Mary Jones";

        //2nd row. Test using this data provider will run twice.
        data[1][0] = 32847;
        data[1][1] = "John Smith";

        return data;
    }

}
