import helpers.Methods;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.Test;
import models.Employee;

@Epic("API Testing")
@Feature("Testing a REST API server with Rest Assured")
@Story("Negative Tests")
@Owner("Mazur N.")
public class NegativeTests extends Methods {

    @Test (description =  "Bad URL")
    @Description("Access a bad URL (part of which is randomly generated)")
    public void negBadURL() {
        verify("status code 200 is returned",
                getStatus("/" + RandomStringUtils.randomAlphabetic(5)), 404, true);
    }

    @Test (description =  "Delete w/o params")
    @Description("Access delete method without any parameters. Should return 'Not found'")
    public void negDelNoParams() {
        verify("deletion is confirmed",
                delEmployee("delete", ""), "Employee not found", true);
    }

    @Test (description =  "Delete w/ wrong params")
    @Description("Pass wrong parameters - index instead of name. Should return 'Not found'")
    public void negDelRandomName() {
        verify("deletion is confirmed",
                delEmployee("delete.name", RandomStringUtils.randomAlphabetic(5)), "Employee not found", true);
    }

    @Test (description =  "Bad input: Empty name")
    @Description("Add an employee without a required field (name is missing)")
    public void negAddEmpl() {
        addEmployee(new Employee(RandomUtils.nextInt(1000, 10000), "",
                        faker.company().profession(), RandomUtils.nextInt(18, 80)), 400);
    }

    @Test (description =  "Bad input: Numbers in text")
    @Description("Add an employee, title has number instead of letters")
    public void negTextInput() {
        addEmployee(new Employee(RandomUtils.nextInt(1000, 10000), faker.name().fullName(),
                        RandomStringUtils.randomNumeric(3), RandomUtils.nextInt(18, 80)), 400);
    }

    @Test (description =  "Bad input: blank name")
    @Description("Add an employee, name does not contain any letters, only spaces")
    public void negTextSpaces() {
        addEmployee(new Employee
                (RandomUtils.nextInt(1000, 10000), "   ", faker.company().profession(), RandomUtils.nextInt(18, 80)), 400);
    }
}
