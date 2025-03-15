package org.example.DataModel;

import java.io.Serializable;
import java.util.Objects;

public class Employee implements Serializable {
    private int idEmployee;
    private String name;

    public Employee(String name, int idEmployee) {
        this.name = name;
        this.idEmployee = idEmployee;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //For serialization
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return idEmployee == employee.idEmployee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmployee);
    }
}
