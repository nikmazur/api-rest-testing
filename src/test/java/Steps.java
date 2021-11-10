import com.github.javafaker.Faker;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import models.Employee;
import models.EmployeeList;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import server.RunServer;

import javax.xml.bind.JAXB;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static helpers.Methods.getRandomBirthday;
import static helpers.ServerConfig.CONF;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class Steps {

    public static Faker faker = new Faker();

    @BeforeSuite
    @Step("Launch REST server, set up Rest Assured")
    public static void launchServer() throws IOException {
        RunServer.main(new String[]{"testing"});

        // Get server address & port from properties
        RestAssured.baseURI = "http://" + CONF.address() + ":" + CONF.port();
        // For parsing POJO from JSON
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterSuite
    @Step("Generate new Employees for the next run")
    public void generateEmployees() {
        // Writes random employee data to local xml file (will be used in the next run)
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get("employees.xml"))) {
            List<Employee> emps = new ArrayList<>();
            for(int i = 0; i <= 2; i++)
                emps.add(genNewEmpl());

            JAXB.marshal( new EmployeeList(emps), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Step("Generate new Employee with random data")
    public static Employee genNewEmpl() {
        Employee empl = new Employee
                (RandomUtils.nextInt(1000, 10000), faker.name().fullName(), faker.company().profession(), getRandomBirthday(1970, 18));
        Allure.addAttachment("New Employee Data", empl.toString());
        return empl;
    }

    //Separate method for assertions, needed to attach as step to report. String 'check' is added to step in place of {0}
    @Step("Verify that {0}")
    public void verify(String check, Object o1, Object o2, boolean equals) {
        if(equals)
            assertEquals(o1, o2);
        else
            assertNotEquals(o1, o2);
    }

    //Data provider used in the 'checkEmployee' test
    @DataProvider
    public Object[][] getEmpl()
    {
        Object[][] data = new Object[3][2];

        //Read employee data from local XML file
        try(BufferedReader reader = Files.newBufferedReader(Paths.get("employees.xml"))){
            EmployeeList employeeList = JAXB.unmarshal(reader, EmployeeList.class);
            for(int i = 0; i <= 2; i++) {
                data[i][0] = employeeList.getEmployees().get(i).getId();
                data[i][1] = employeeList.getEmployees().get(i).getName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

}
