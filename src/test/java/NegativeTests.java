import io.qameta.allure.*;
import models.Employee;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.Test;

import static helpers.Methods.getRandomBirthday;
import static helpers.ServerRequests.*;

@Epic("API Testing")
@Feature("Testing a REST API server with Rest Assured")
@Story("Negative Tests")
@Owner("Mazur N.")
public class NegativeTests extends Steps {

    @Test(description =  "Bad URL")
    @Description("Access a bad URL (part of which is randomly generated)")
    public void negBadURL() {
        verify("status code 404 is returned",
                getStatus("/" + RandomStringUtils.randomAlphabetic(5)), 404, true);
    }

    @Test(description =  "Delete w/o params")
    @Description("Access delete method without any parameters. Should return status code 400")
    public void negDelNoParams() {
        delEmployee("delete", "", 400);
    }

    @Test(description =  "Bad input: Numbers in text")
    @Description("Add an employee, title has number instead of letters")
    public void negTextInput() {
        Employee badTitle = new Employee(RandomUtils.nextInt(1000, 10000), faker.name().fullName(),
                RandomStringUtils.randomNumeric(3), getRandomBirthday(1970, 18));
        addEmployee(badTitle, 400);
    }

    @Test(description =  "Bad input: Blank name")
    @Description("Add an employee, name does not contain any letters, only spaces")
    public void negTextSpaces() {
        Employee noName = new Employee
                (RandomUtils.nextInt(1000, 10000), "  ", faker.company().profession(), getRandomBirthday(1970, 18));
        addEmployee(noName, 400);
    }
}
