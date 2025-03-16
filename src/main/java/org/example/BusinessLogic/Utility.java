package org.example.BusinessLogic;

import org.example.DataModel.ComplexTask;
import org.example.DataModel.Employee;
import org.example.DataModel.SimpleTask;
import org.example.DataModel.Task;

import java.util.Collections;
import java.util.*;

public class Utility {


    //Calculates the number of completed and uncompleted tasks for an employee.
    public static Map<String, Integer> countCompletedAndUncompletedTasks(Map<Employee, List<Task>> employeeTasks, Employee employee){
        Map<String, Integer> completedAndUncompletedTasks = new HashMap<>();
        int completed = 0;
        int uncompleted = 0;
        List<Task> tasks = employeeTasks.getOrDefault(employee, new ArrayList<>());
        for(Task task : tasks){
            if(task.getStatusTask().equals("Completed"))
                completed++;
            else
                uncompleted++;
        }
        completedAndUncompletedTasks.put("Completed", completed);
        completedAndUncompletedTasks.put("Uncompleted", uncompleted);
        return completedAndUncompletedTasks;
    }



    //Genrates a filtered list with all the employees with more than 40 work hours
    public static List<Employee> getSortedEmployeesByWorkHours(TasksManagement controller){
        List<Employee> employees = controller.getAllEmployees();
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()){
            Employee employee = iterator.next();
            if( (controller.calculateEmployeeWorkDuration(employee.getIdEmployee())) <= 40)
                iterator.remove();
        }

        //Sorting the employees with more than 40 hour work duration
        Collections.sort(employees, new Comparator<Employee>() {
            public int compare(Employee o1, Employee o2) {
                return Integer.compare(controller.calculateEmployeeWorkDuration(o1.getIdEmployee()),
                        controller.calculateEmployeeWorkDuration(o2.getIdEmployee())
                );
            }
        });
        return employees;
    }
}
