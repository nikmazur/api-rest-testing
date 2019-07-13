import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.Test;
import springrest.Employee;

import static org.testng.Assert.assertEquals;

@Epic("API Testing")
@Feature("Testing a REST API server with Rest Assured")
@Story("Negative Tests")
public class NegativeTests extends Methods {

    //Try to access a bad URL (part of which is randomly generated).
    @Test()
    public void negBadURL() {
        assertEquals(Methods.getStatus("/" + RandomStringUtils.randomAlphabetic(5)), 200);
    }

    //Try to access delete method without any parameters. Expecting "Not found" message in return.
    @Test ()
    public void negDelNoParams() {
        assertEquals(delEmployee("", null), "Employee deleted");
    }

    //Try to pass wrong parameters - index instead of name. Expecting "Not found" in return.
    @Test ()
    public void negDelWrongArg() {
        assertEquals(delEmployee("name", "0"), "Employee deleted");
    }

    /* Bad input. Adding an employee without a required field (name is missing).
     * In this test we're also expecting an exception by param 'expectedExceptions'
     * (Request will return status code 400 instead of 200, which will trigger an Assertion exception).*/
    @Test ()
    public void negAddEmpl() {
        Employee newEmpl = new Employee
                (RandomUtils.nextInt(0, 10000), null, faker.company().profession(), RandomUtils.nextInt(18, 100));
        addEmployee(newEmpl);
    }

    //Bad input. Field salary contains letters instead of numbers.
    @Test ()
    public void negTextInput() {
        faker.number().numberBetween(18, 100);
        Employee newEmpl = new Employee
                (0, faker.name().fullName(), faker.company().profession(), RandomUtils.nextInt(18, 100));
        addEmployee(newEmpl);
    }

    //Bad input. Name does not contain any letters, only spaces.
    @Test ()
    public void negTextSpaces() {
        Employee newEmpl = new Employee
                (RandomUtils.nextInt(0, 10000), "   ", faker.company().profession(), RandomUtils.nextInt(18, 100));
        addEmployee(newEmpl);
    }
}
