import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import springrest.Employee;

import static org.testng.Assert.*;

@Epic("API Testing")
@Feature("Testing a REST API server with Rest Assured")
@Story("Positive Tests")
public class PositiveTests extends Methods {

    /* All subsequent tests are dependent on this one (dependsOnMethods argument).
     * Will sometimes fail because of the random bool in Data. */
    @Test(description = "Smoke test for  server availability. Checks for the HTTP 200 status code.")
    public void ping() {
        try {
            //Form HTTP request, check response status code. Comment this to disable random failing.
            assertEquals(Methods.getStatus("/ping"), 200);
        }
        catch(AssertionError ae) {
            //Example of a custom message upon test failure using try-catch blocks
            Assert.fail("Ping Failed! Resource was not available.");
        }
    }

    @Test (description =  "Verify that the starting Employees set is not empty", dependsOnMethods = "ping")
    public void notEmpty() {
        assertFalse(getEmployees().isEmpty());
    }

    //Check the contents of JSON for specific data (in this case the name of employee by their index).
    //This test uses a data provider 'getEmpl' (listed in Methods) and will run multiple times.
    @Test (dataProvider = "getEmpl", dependsOnMethods = "ping")
    public void checkEmployee(final int ID, final String NAME) {

        assertNotEquals(getEmployees().stream()
                .filter(x -> x.getId() == ID && x.getName().equals(NAME))
                .count(), (long) 0);
    }

    //Check the functionality of adding a new employee.
    @Test (dependsOnMethods = "ping")
    public void addNewEmployee() {
        Employee newEmpl = new Employee
                (RandomUtils.nextInt(0, 10000), faker.name().fullName(), faker.company().profession(), RandomUtils.nextInt(18, 100));
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
    public void nameComma() {
        String commaName = faker.name().firstName() + ", " + faker.name().lastName();

        Employee newEmpl = new Employee
                (RandomUtils.nextInt(0, 10000), commaName, faker.company().profession(), RandomUtils.nextInt(18, 100));

        assertNotEquals(addEmployee(newEmpl).stream()
                .filter(x -> x.getName().equals(commaName.replace(",", "")))
                .count(), (long) 0);
    }

}
