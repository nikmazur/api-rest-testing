package springrest;

import org.apache.commons.lang3.RandomUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;


public class Data {

    //Bool for random ping results.
    public final boolean PING_RAND;

    //HashMap for storing data about our Employees + getter
    private static ArrayList<Employee> comp = new ArrayList<>();
    public static ArrayList<Employee> getComp() {
        return comp;
    }

    //Constructor: 1. Sets bool to random value 2. Populates ArrayList with initial Employees data
    public Data() {
        PING_RAND = RandomUtils.nextBoolean();

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
                    Double.parseDouble(prop.getProperty("Age"))));
        }
    }

    //Method for adding new employees to the map.
    public static void addEmpl(int ID, String name, String title, double age) {
        comp.add(new Employee(ID, name, title, age));
    }

    //Delete the employee by index number.
    public static void delEmpl(int index) {
        comp.remove(index);
    }

    /* Delete employee by name. Uses an iterator to traverse all name fields and check for matches.
     * If match is found, it is deleted through the iterator and true is returned. */
    public static boolean delEmplName(String name) {
        if(comp.removeIf(x -> x.getName().equals(name)))
            return true;
        else
            return false;
    }

}
