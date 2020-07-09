package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Employee {

    private Integer id;
    private String name;
    private String title;
    private Integer age;

    // For attaching data to report
    @Override
    public String toString() {
        return "Employee: id=" + id + ", name=" + name +", title=" + title + ", age=" + age;
    }
}
