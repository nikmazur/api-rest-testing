package springrest;

import java.util.HashMap;
import java.util.Random;

public class Data {

    private static boolean pingRand;
    protected static boolean isPingRand() {
        return pingRand;
    }

    private static HashMap<Integer, HashMap<String, String>> comp = new HashMap<>();
    public static HashMap<Integer, HashMap<String, String>> getComp() {
        return comp;
    }

    public Data() {
        Random random = new Random();
        pingRand = random.nextBoolean();

        HashMap<String, String> emp1 = new HashMap<>();
        emp1.put("name", "Mary Jones");
        emp1.put("age", "39");
        emp1.put("salary", "79400");
        comp.put(0, emp1);

        HashMap<String, String> emp2 = new HashMap<>();
        emp2.put("name", "John Smith");
        emp2.put("age", "27");
        emp2.put("salary", "67500");
        comp.put(1, emp2);
    }

    public static int addEmpl(String name, String age, String salary) {
        int ind = comp.size();

        HashMap<String, String> newEmpl = new HashMap<>();
        newEmpl.put("name", name);
        newEmpl.put("age", age);
        newEmpl.put("salary", salary);
        comp.put(ind++, newEmpl);

        return ind;
    }

    protected void pingOk(HashMap<String, String> res) {
        res.put("result", "200");
        res.put("comment", "OK");
    }
}
