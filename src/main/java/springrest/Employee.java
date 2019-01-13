package springrest;

public class Employee {

    private String name;
    private double age;
    private double salary;

    public Employee(String name, double age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public double getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

}
