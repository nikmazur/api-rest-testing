package springrest;

import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Data {

    //Bool for random ping results.
    public final boolean PING_RAND;

    //HashMap for storing data about our Employees + getter
    private static HashMap<Integer, Employee> comp = new HashMap<>();
    public static HashMap<Integer, Employee> getComp() {
        return comp;
    }

    //Constructor: 1. Sets bool to random value 2. Populates HashMap with initial Employees data
    public Data() {
        PING_RAND = RandomUtils.nextBoolean();

        Employee emp1 = new Employee("Mary Jones", 39, 79400);
        comp.put(0, emp1);

        Employee emp2 = new Employee("John Smith", 27, 67500);
        comp.put(1, emp2);
    }

    //Method for adding new employees to the map.
    public static void addEmpl(String name, double age, double salary) {

        //Cycle through the map to get the largest index.
        //We'll use that number +1 to make sure we add and not overwrite new values.
        int ind = 0;
        for (int i : comp.keySet()) {
            if(i > ind)
                ind = i;
        }

        Employee newEmpl = new Employee(name, age, salary);
        comp.put(++ind, newEmpl);
    }

    //Delete the employee by index number.
    public static void delEmpl(int index) {
        comp.remove(index);
    }

    /* Delete employee by name. Uses an iterator to traverse all name fields and check for matches.
     * If match is found, it is deleted through the iterator and true is returned. */
    public static boolean delEmplName(String name) {

        Iterator iter = comp.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry pair = (Map.Entry)iter.next();
            Employee empl = (Employee) pair.getValue();

            if(empl.getName().equals(name)) {
                iter.remove();
                return true;
            }
        }

        return false;
    }

}
