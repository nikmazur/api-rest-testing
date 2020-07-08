package server;

import models.Employee;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class Data {

    // List for storing data about our employees
    private static List<Employee> comp = new ArrayList<>();

    public static List<Employee> getComp() {
        return comp;
    }

    // Populates ArrayList with initial Employees data
    public static void initEmployees() {
        for(int i = 1; i < 4; ++i) {
            Properties prop = new Properties();
            try {
                Reader propReader = Files.newBufferedReader(Paths.get("randomEmployees/Empl" + i + ".properties"));
                prop.load(propReader);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            comp.add(new Employee(
                    Integer.parseInt(prop.getProperty("ID")),
                    prop.getProperty("Name"),
                    prop.getProperty("Title"),
                    Integer.parseInt(prop.getProperty("Age"))));
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
