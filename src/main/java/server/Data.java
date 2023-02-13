package server;

import lombok.experimental.UtilityClass;
import models.Employee;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


// Lombok annotation for safely handling utility classes (adds private constructor)
@UtilityClass
public class Data {

    // List for storing data about our employees
    private static List<Employee> comp = new ArrayList<>();

    public static List<Employee> getComp() {
        return comp;
    }

    // Populates ArrayList with initial Employees data
    public static void initEmployees() {
        for(int i = 1; i < 4; i++) {
            Properties prop = new Properties();
            try(Reader propReader = Files.newBufferedReader(Paths.get("randomEmployees/Empl" + i + ".properties"))) {
                prop.load(propReader);
            } catch (IOException ioe) {
                Logger.getGlobal().log(Level.SEVERE, ioe.getMessage());
            }

            comp.add(new Employee(
                    Integer.parseInt(prop.getProperty("ID")),
                    prop.getProperty("Name"),
                    prop.getProperty("Title"),
                    prop.getProperty("Birthday")));
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
