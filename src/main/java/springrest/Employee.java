package springrest;

public class Employee {

    private int id;
    private String name;
    private String title;
    private int age;

    public Employee(int id, String name, String title, int age) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getAge() {
        return age;
    }


    @Override
    public String toString() {
        return "Employee: id=" + id + ", name=" + name +", title=" + title + ", age=" + age;
    }

    //Equals method for comparing class objects
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Employee)) {
            return false;
        }

        Employee empl = (Employee) o;

        //Returns true if all params in both objects are equal, false otherwise
        return empl.id == (id) &&
                empl.name.equals(name) &&
                empl.title.equals(title) &&
                empl.age == (age);
    }

}
