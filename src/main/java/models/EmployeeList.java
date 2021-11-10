package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeList {
    @XmlElement(name="employee")
    private List<Employee> employees;
}
