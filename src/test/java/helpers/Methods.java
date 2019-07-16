package helpers;

import com.github.javafaker.Faker;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import springrest.Application;
import springrest.Employee;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Methods {

    public static Faker faker;

    @BeforeSuite
    @Step("Launch the Spring REST server")
    public void launchServer() {
        Application.main(new String[]{""});
        faker = new Faker();
        RestAssured.baseURI = "http://localhost:8188";
    }

    @AfterSuite
    @Step("Generate new Employees for the next run")
    public void generateEmployees() throws IOException {

        for(int i = 1; i < 4; ++i) {
            Properties emplProp = new Properties();
            emplProp.setProperty("ID", String.valueOf(RandomUtils.nextInt(1, 10000)));
            emplProp.setProperty("Name", faker.name().fullName());
            emplProp.setProperty("Title", faker.company().profession());
            emplProp.setProperty("Age", String.valueOf(RandomUtils.nextInt(18, 80)));

            File file = new File("randomEmployees/Empl" + i + ".properties");
            FileWriter writer = new FileWriter(file);
            emplProp.store(writer,"Employee Data");
            writer.close();
        }
    }

    public static RequestSpecification mainRequest() {
        return given().baseUri(RestAssured.baseURI).contentType(ContentType.JSON).accept(ContentType.JSON);
    }

    @Step("Get server status")
    public static int getStatus(String path) {
            return mainRequest().basePath(path).get().getStatusCode();
    }

    @Attachment
    @Step("Retrieve all employees, return as List")
    public static List<Employee> getEmployees() {
        return Arrays.asList(mainRequest().basePath("/employees").get().then().assertThat().statusCode(200).extract().as(Employee[].class));
    }

    @Attachment
    @Step("Add new employee, return new list of employees")
    public static List<Employee> addEmployee(Employee empl) {
        return Arrays.asList(mainRequest().basePath("/employees/add").body(empl).post()
                .then().assertThat().statusCode(200).extract().as(Employee[].class));
    }

    @Attachment
    @Step("Request to delete an employee (by Name or Index)")
    public static String delEmployee(String type, Object id) {
        return mainRequest().basePath("/employees/delete").queryParam(type, id).post()
                .then().assertThat().statusCode(200).extract().asString();
    }

    //Data provider used in the 'checkEmployee' test
    @DataProvider
    public Object[][] getEmpl()
    {
        Object[][] data = new Object[3][2];

        for(int i = 1; i < 4; ++i) {
            Properties prop = new Properties();
            try {
                Reader propReader = Files.newBufferedReader(Paths.get("randomEmployees/Empl" + i + ".properties"));
                prop.load(propReader);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            data[i-1][0] = Integer.parseInt(prop.getProperty("ID"));
            data[i-1][1] = prop.getProperty("Name");
        }

        return data;
    }

}
