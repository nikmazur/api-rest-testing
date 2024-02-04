import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import models.Employee;
import net.datafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import server.RunServer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static helpers.Methods.getRandomBirthday;
import static helpers.ServerConfig.CONF;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

public class Steps {

    public static Faker faker = new Faker();

    @BeforeSuite
    @Step("Launch REST server, set up Rest Assured")
    public void launchServer() throws IOException {

        RunServer.main(new String[]{"testing"});

        // Get server address & port from properties
        RestAssured.baseURI = "http://" + CONF.address() + ":" + CONF.port();
        // For parsing POJO from JSON
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterSuite
    @Step("Generate new Employees for the next run")
    public void generateEmployees() throws IOException {

        // Writes random employee data to local prop files (will be used in the next run)
        for(int i = 1; i < 4; ++i) {
            var empl = genNewEmpl();
            var emplProp = new Properties();

            emplProp.setProperty("ID", String.valueOf(empl.getId()));
            emplProp.setProperty("Name", empl.getName());
            emplProp.setProperty("Title", empl.getTitle());
            emplProp.setProperty("Birthday", empl.getBirthday());

            File file = new File("randomEmployees/Empl" + i + ".properties");
            FileWriter writer = new FileWriter(file);
            emplProp.store(writer,"Employee Data");
            writer.close();
        }
    }

    @Step("Generate new Employee with random data")
    public Employee genNewEmpl() {
        var empl = new Employee
                (RandomUtils.nextInt(1000, 10000), faker.name().fullName(), faker.company().profession(), getRandomBirthday(1971, 18));
        Allure.addAttachment("New Employee Data", empl.toString());
        return empl;
    }

    //Separate wrapper method for assertions, needed to attach as step to report. String 'check' is added to step in place of {0}
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
        var data = new Object[3][2];

        //Read employee data from local files
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
