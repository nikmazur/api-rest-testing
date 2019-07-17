import helpers.Methods;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.Test;
import springrest.Employee;

import static org.testng.Assert.assertEquals;

@Epic("API Testing")
@Feature("Testing a REST API server with Rest Assured")
@Story("Negative Tests")
@Owner("Mazur N.")
public class NegativeTests extends Methods {

    @Test (description =  "Bad URL")
    @Description("Access a bad URL (part of which is randomly generated).")
    public void negBadURL() {
        assertEquals(Methods.getStatus("/" + RandomStringUtils.randomAlphabetic(5)), 200);
    }

    @Test (description =  "Delete w/o params")
    @Description("Access delete method without any parameters. Should return 'Not found'")
    public void negDelNoParams() {
        assertEquals(delEmployee("", null), "Employee deleted");
    }

    @Test (description =  "Delete w/ wrong params")
    @Description("Pass wrong parameters - index instead of name. Should return 'Not found'")
    public void negDelWrongArg() {
        assertEquals(delEmployee("name", "0"), "Employee deleted");
    }

    @Test (description =  "Bad input: null name")
    @Description("Add an employee without a required field (name is missing).")
    public void negAddEmpl() {
        addEmployee(new Employee
                (RandomUtils.nextInt(1000, 10000), null, faker.company().profession(), RandomUtils.nextInt(18, 80)));
    }

    @Test (description =  "Bad input: numbers in text")
    @Description("Add an employee, title has number instead of letters.")
    public void negTextInput() {
        addEmployee(new Employee
                (RandomUtils.nextInt(1000, 10000), faker.name().fullName(), RandomStringUtils.randomNumeric(3), RandomUtils.nextInt(18, 80)));
    }

    @Test (description =  "Bad input: blank name")
    @Description("Add an employee, name does not contain any letters, only spaces.")
    public void negTextSpaces() {
        addEmployee(new Employee
                (RandomUtils.nextInt(1000, 10000), "   ", faker.company().profession(), RandomUtils.nextInt(18, 80)));
    }
}
