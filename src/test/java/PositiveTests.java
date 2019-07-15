import io.qameta.allure.*;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import springrest.Employee;

import static org.testng.Assert.*;

@Epic("API Testing")
@Feature("Testing a REST API server with Rest Assured")
@Story("Positive Tests")
public class PositiveTests extends Methods {

    @Test(description = "Smoke availability")
    @Severity(value = SeverityLevel.CRITICAL)
    @Description("Checks for the HTTP 200 status code. Will fail randomly.")
    @Flaky
    public void ping() {
        try {
            //Comment this to disable random failing.
            assertEquals(Methods.getStatus("/ping"), 200);
        }
        catch(AssertionError ae) {
            //Example of a custom message upon test failure using try-catch blocks
            Assert.fail("Ping Failed! Resource was not available.");
        }
    }

    @Test (description =  "Employees not empty", dependsOnMethods = "ping")
    @Description("Verify that the starting Employees set is not empty.")
    public void notEmpty() {
        assertFalse(getEmployees().isEmpty());
    }


    @Test (description =  "Check employee", dataProvider = "getEmpl")
    @Description("Check the contents of JSON for specific data (name & index of an employee).")
    //This test uses a data provider 'getEmpl' (listed in Methods) and will run multiple times.
    public void checkEmployee(int ID, String NAME) {
        assertNotEquals(getEmployees().stream()
                .filter(x -> x.getId() == ID && x.getName().equals(NAME))
                .count(), (long) 0);
    }

    @Test (description =  "Add new employee")
    @Description("Check the functionality of adding a new employee.")
    public void addNewEmployee() {
        Employee newEmpl = new Employee
                (RandomUtils.nextInt(1, 10000), faker.name().fullName(), faker.company().profession(), RandomUtils.nextInt(18, 100));
        assert(addEmployee(newEmpl).stream().anyMatch(x -> x.equals(newEmpl)));
    }


    /* This and the next test are set to run only after the other tests which are dependent on content
     * have been passed, so as not to interfere with them and cause a false fail. */
    @Test (description =  "Remove employee by Index", dependsOnMethods = {"ping", "notEmpty", "checkEmployee"})
    public void delEmployeeIndex() {
        assertEquals(delEmployee("ind", 1), "Employee deleted");
    }

    //Try to remove an employee by their name
    @Test (description =  "Remove employee by Name", dependsOnMethods = {"ping", "notEmpty", "checkEmployee"})
    public void delEmployeeName() {
        assertEquals(delEmployee("name", getEmpl()[1][1]), "Employee deleted");
    }


    @Test (description =  "Comma in Name")
    @Description("Add a new employee with the Name separated by a comma (sent as a String). " +
            "Comma will be removed and the employee will be added successfully.")
    public void nameComma() {
        String commaName = faker.name().firstName() + ", " + faker.name().lastName();

        Employee newEmpl = new Employee
                (RandomUtils.nextInt(1, 10000), commaName, faker.company().profession(), RandomUtils.nextInt(18, 100));

        assertNotEquals(addEmployee(newEmpl).stream()
                .filter(x -> x.getName().equals(commaName.replace(",", "")))
                .count(), (long) 0);
    }

    @Test (description =  "Check employee age")
    @Description("Verify that all employees are 18 or older.")
    public void checkAge() {
        assertEquals(getEmployees().stream()
                .filter(x -> x.getAge() < 18)
                .count(), (long) 0);
    }

    @Test (description =  "Zero ID")
    @Description("Verify that no employee has an ID = 0")
    public void zeroId() {
        assertEquals(getEmployees().stream()
                .filter(x -> x.getId() == 0)
                .count(), (long) 0);
    }

}
