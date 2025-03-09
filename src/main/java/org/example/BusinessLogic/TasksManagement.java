package org.example.BusinessLogic;

import org.example.DataModel.Employee;
import org.example.DataModel.Task;

import java.util.*;

public class TasksManagement {
    private Map<Employee, List<Task>> map;

    public TasksManagement() {
        map = new HashMap<>();
    }

    public void assignTaskToEmployee(int idEmployee, Task task){

    }

    public int calculateEmployeeWorkDuration(int idEmployee){
        return 0;
    }

    public void modifyTaskStatus(int idEmployee, int idTask){

    }

}
