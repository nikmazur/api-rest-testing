package springrest;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;


public class Data {

    //ArrayList for storing data about our Employees + getter
    private static ArrayList<Employee> comp = new ArrayList<>();
    public static ArrayList<Employee> getComp() {
        return comp;
    }

    //Constructor populates ArrayList with initial Employees data
    public Data() {
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

    public static void addEmpl(int ID, String name, String title, int age) {
        comp.add(new Employee(ID, name, title, age));
    }

    public void delEmpl(int index) {
        comp.remove(index);
    }

    public boolean delEmplName(String name) {
        return comp.removeIf(x -> x.getName().equals(name));
    }

}
