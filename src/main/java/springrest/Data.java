package springrest;

import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;


public class Data {

    //Bool for random ping results.
    public final boolean PING_RAND;
    private static TestData data;

    //HashMap for storing data about our Employees + getter
    private static ArrayList<Employee> comp = new ArrayList<>();
    public static ArrayList<Employee> getComp() {
        return comp;
    }

    //Constructor: 1. Sets bool to random value 2. Populates HashMap with initial Employees data
    public Data() {
        PING_RAND = RandomUtils.nextBoolean();

        data = ConfigFactory.create(TestData.class);
        comp.add(0, new Employee(data.emp1ID(), data.emp1Name(), data.emp1Title(), data.emp1Age()));
        comp.add(1, new Employee(data.emp2ID(), data.emp2Name(), data.emp2Title(), data.emp2Age()));
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
