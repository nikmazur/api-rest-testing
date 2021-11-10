package server;

import models.Employee;
import models.EmployeeList;

import javax.xml.bind.JAXB;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Data {

    // List for storing data about employees
    private static List<Employee> comp = new ArrayList<>();

    public static List<Employee> getComp() {
        return comp;
    }

    // Populates List with initial Employees data
    public static void initEmployees() {
        try(BufferedReader reader = Files.newBufferedReader(Paths.get("employees.xml"))){
            EmployeeList employeeList = JAXB.unmarshal(reader, EmployeeList.class);
            comp = employeeList.getEmployees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addEmpl(Employee empl) {
        comp.add(empl);
    }

    public static void delEmplIndex(int index) {
        comp.removeIf(x -> x.getId() == index);
    }

    public static void delEmplName(String name) {
        comp.removeIf(x -> x.getName().equals(name));
    }

}
