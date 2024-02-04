import io.qameta.allure.*;
import models.Employee;
import org.apache.commons.lang3.RandomUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.function.Predicate;

import static helpers.Methods.compareDates;
import static helpers.Methods.getRandomBirthday;
import static helpers.ServerRequests.*;

@Epic("API Testing")
@Feature("Testing a REST API server with Rest Assured")
@Story("Positive Tests")
@Owner("Mazur N.")
public class PositiveTests extends Steps {

    // Priority is for running this test first. @Severity and @Description are for the report.
    @Test(description = "Smoke availability", priority = 1)
    @Severity(value = SeverityLevel.CRITICAL)
    @Description("Check REST server availability.")
    public void ping() {
        verify("status code 200 is returned", getStatus("/ping"), 200, true);
    }

    @Test(description =  "Employees not empty", dependsOnMethods = "ping")
    @Description("Verify that the starting Employees list is not empty.")
    public void notEmpty() {
        verify("the response is not empty", getEmployees().size(), 0, false);
    }


    // This test uses a data provider 'getEmpl' (listed in Steps) and will run multiple times.
    @Test(description =  "Check employee", dataProvider = "getEmpl")
    @Description("Check the contents of JSON for specific data (name & index of an employee).")
    public void checkEmployee(int ID, String NAME) {
        // Example of a separate predicate for easier reading
        Predicate<Employee> matchIDName = x -> x.getId() == ID && x.getName().equals(NAME);

        verify("that there is a match in the list for each employee",
                // 0 is type cast to long because count() returns long. Comparing int to long would cause a false positive
                getEmployees().stream().filter(matchIDName).count(), (long) 0, false);
    }

    @Test(description =  "Add new employee")
    @Description("Check the functionality of adding a new employee.")
    public void addNewEmployee() {
        var newEmpl = genNewEmpl();
        verify("the new employee is listed in the returned list",
                addEmployee(newEmpl, 201).stream().anyMatch(x -> x.equals(newEmpl)), true, true);
    }

    /* This and the next test are set to run only after the other tests which are dependent on content
     * have been passed, so as not to interfere with them and cause a false fail. */
    @Test(description =  "Remove employee by Index", dependsOnMethods = {"ping", "notEmpty", "checkEmployee"})
    public void delEmployeeIndex() {
        Predicate<Employee> matchIndex = x -> x.getId() == (int)(getEmpl()[0][0]);

        verify("deletion is confirmed", delEmployee("delete.ind", getEmpl()[0][0], 200)
                        .stream().anyMatch(matchIndex), false, true);
    }

    @Test(description =  "Remove employee by Name", dependsOnMethods = {"ping", "notEmpty", "checkEmployee"})
    public void delEmployeeName() {
        Predicate<Employee> namesAreEqual = x -> x.getName().equals(getEmpl()[0][1]);

        verify("deletion is confirmed",
                delEmployee("delete.name", getEmpl()[0][1], 200).stream().anyMatch(namesAreEqual), false, true);
    }

    @Test(description =  "Comma in Name")
    @Description("Add a new employee with the Name separated by a comma. " +
            "Comma will be removed and the employee will be added successfully.")
    public void nameComma() {
        var commaName = faker.name().firstName() + ", " + faker.name().lastName();

        var newEmpl = new Employee
                (RandomUtils.nextInt(1, 10000), commaName, faker.company().profession(), getRandomBirthday(1970, 18));

        verify("name was added without a comma",
                addEmployee(newEmpl, 201).stream()
                        .filter(x -> x.getName().equals(commaName.replace(",", ""))).count(), (long) 0, false);
    }

    @Test(description =  "Check employee age")
    @Description("Verify that all employees are 18 or older.")
    public void checkAge() {
        getEmployees().forEach(x -> verify("there are no employees in the list younger than 18",
                                compareDates(LocalDate.parse(x.getBirthday()), LocalDate.now().minusYears(18)), true, true));
    }

    @Test(description =  "Zero ID")
    @Description("Verify that no employee has an ID 0")
    public void zeroId() {
        verify("there are no employees in the list with ID 0",
                getEmployees().stream().filter(x -> x.getId() == 0).count(), (long) 0, true);
    }

    @Test(description = "Random fail")
    @Description("Check for HTTP 200 status code. Will fail randomly.")
    @Flaky
    public void randomFail() {
        try {
            verify("status code 200 is returned", getStatus("/random"), 200, true);
        } catch(AssertionError ae) {
            // Example of a custom message upon test failure using try-catch blocks
            Assert.fail("Ping Failed! Resource was not available.");
        }
    }

}
