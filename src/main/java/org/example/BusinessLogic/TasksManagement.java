package org.example.BusinessLogic;

import org.example.DataModel.ComplexTask;
import org.example.DataModel.Employee;
import org.example.DataModel.SimpleTask;
import org.example.DataModel.Task;
import org.example.DataAccess.DataS;

import java.io.*;
import java.util.*;

public class TasksManagement {
    private Map<Employee, List<Task>> employeeTasks = new HashMap<>();
    private Map<Integer, Task> tasksMap = new HashMap<>();
    private Map<Integer, Employee> employeesMap = new HashMap<>();



    //Loading data from serialization files
    public TasksManagement() {
        DataS.loadData(employeesMap, tasksMap, employeeTasks);
    }

    //saving data in serialization files
    public void saveDataFromTaskManagement()
    {
        DataS.saveData(employeesMap, tasksMap, employeeTasks);
    }

    public Map<Employee, List<Task>> getEmployeeTasks() {
        return employeeTasks;
    }

    //Working with the employeeMap
    public void addEmployee(Employee employee) {
        employeesMap.put(employee.getIdEmployee(), employee);
        saveDataFromTaskManagement();
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeesMap.values());
    }

    public Employee getEmployeeById(int employeeId) {
        return employeesMap.get(employeeId);
    }

    public void addTask(Task task) {
        tasksMap.put(task.getIdTask(), task);
        saveDataFromTaskManagement();
    }

    //Working with the taskMap
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    public Task getTaskById(int taskId) {
        return tasksMap.get(taskId);
    }

    //Asign task to employee
    public void assignTaskToEmployee(int idEmployee, Task task){
        Employee employee = getEmployeeById(idEmployee);
        employeeTasks.computeIfAbsent(employee, k -> new ArrayList<>()).add(task);
        saveDataFromTaskManagement();
    }

    public List<Task> getTasksByEmployeeId(int employeeId) {
        return employeeTasks.getOrDefault(getEmployeeById(employeeId), new ArrayList<>());
    }

    //Calculates the work hours for an employee
    public int calculateEmployeeWorkDuration(int idEmployee){
        Employee employee = getEmployeeById(idEmployee);
        List<Task> employeeTaskList = employeeTasks.get(employee);
        return totalWorkHoursInList(employeeTaskList);
    }

    //Calculates the work hours in a list
    private int totalWorkHoursInList(List<Task> tasks) {
        if (tasks == null) return 0;
        int totalWorkHours = 0;
        for(Task task : tasks){
            if (task instanceof SimpleTask && task.getStatusTask().equals("Completed")){
                totalWorkHours += ((SimpleTask)task).estimateDuration();
            } else if (task instanceof ComplexTask){
                totalWorkHours += totalWorkHoursInList(((ComplexTask) task).getTasks());
            }
        }
        return totalWorkHours;
    }

    //Modifies status for a task from an employee looking through their list of tasks
    public void modifyTaskStatus(int idEmployee, int idTask){
        Employee employee = getEmployeeById(idEmployee);
        List<Task> employeesTaskList = employeeTasks.get(employee);

        if (employeesTaskList != null) {
            boolean updated = modifyTaskFromList(employeesTaskList, idTask);
            if (updated) {
                saveDataFromTaskManagement();
            }
        }
    }

    //Modifies a task from a given list of tasks (even if it is a subtask in a complex task)
    private boolean modifyTaskFromList(List<Task> tasks, int idTask) {
        for (Task task : tasks) {
            if (task.getIdTask() == idTask) {
                task.modifyStatus();
                return true;
            }
            else // daca nu ne uitam ori mai jos in taskul complex, ori prin cele simple
            if (task instanceof ComplexTask) {
                boolean updated = modifyTaskFromList(((ComplexTask) task).getTasks(), idTask);
                if (updated) return true;
            }
        }
        return false;
    }
}
