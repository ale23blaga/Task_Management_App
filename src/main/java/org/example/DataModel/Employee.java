package org.example.DataModel;

public class Employee {
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
}
