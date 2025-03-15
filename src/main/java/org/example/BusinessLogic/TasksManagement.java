package org.example.BusinessLogic;

import org.example.DataModel.ComplexTask;
import org.example.DataModel.Employee;
import org.example.DataModel.SimpleTask;
import org.example.DataModel.Task;

import java.io.*;
import java.util.*;

public class TasksManagement {
    private Map<Employee, List<Task>> employeeTasks = new HashMap<>();

    private Map<Integer, Task> tasksMap = new HashMap<>();
    private Map<Integer, Employee> employeesMap = new HashMap<>();

    //Serialization
    private static final String employeeFile = "employees.ser";
    private static final String taskFile = "tasks.ser";
    private static final String assignmentsFile = "assignments.ser";

    //Constructor
    public TasksManagement() {
        loadData();
    }

    public Map<Employee, List<Task>> getEmployeeTasks() {
        return employeeTasks;
    }

    //Working with the employeeMap
    public void addEmployee(Employee employee) {
        employeesMap.put(employee.getIdEmployee(), employee);
        saveData();
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeesMap.values());
    }

    public Employee getEmployeeById(int employeeId) {
        return employeesMap.get(employeeId);
    }

    public void addTask(Task task) {
        tasksMap.put(task.getIdTask(), task);
        saveData();
    }

    //Working with the taskMap
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    public Task getTaskById(int taskId) {
        return tasksMap.get(taskId);
    }

    public List<Task> getTasksByEmployeeId(int employeeId) {
        return employeeTasks.getOrDefault(getEmployeeById(employeeId), new ArrayList<>());
    }

    public void assignTaskToEmployee(int idEmployee, Task task){
        Employee employee = getEmployeeById(idEmployee);
        employeeTasks.computeIfAbsent(employee, k -> new ArrayList<>()).add(task);
        saveData();
    }

    public int calculateEmployeeWorkDuration(int idEmployee){
        Employee employee = getEmployeeById(idEmployee);
        List<Task> employeesTaskList = employeeTasks.get(employee);
        return calculateTasksWorkDuration(employeesTaskList);
    }

    public void modifyTaskStatus(int idEmployee, int idTask){
        Employee employee = getEmployeeById(idEmployee);
        List<Task> employeesTaskList = employeeTasks.get(employee);
        modifyTaskFromList(employeesTaskList, idTask); // schimba statusul la un task dintr-o lista
        saveData();
    }

    // functiile de mai jos presupun ca ar trebui sa mearga in utility sau ceva
    private void modifyTaskFromList(List<Task> tasks, int idTask)
    {
        for (Task task : tasks) {
            if (task.getIdTask() == idTask) {
                task.modifyStatus();
                return;
            }
            else // daca nu ne uitam ori mai jos in taskul complex, ori prin cele simple
            if (task instanceof ComplexTask) {
                modifyTaskFromList(((ComplexTask) task).getTasks(), idTask);
            }
        }
    }

    private int calculateTasksWorkDuration(List<Task> tasks)
    {
        int total =  0;
        for( Task task : tasks )
        {
            if (task instanceof ComplexTask)
            {
                total += calculateTasksWorkDuration(((ComplexTask) task).getTasks());
            }
            else
            {
                if (task.getStatusTask().equals("Completed"))
                {
                    total += ((SimpleTask) task).estimateDuration();
                }
            }
        }
        return total;
    }

    //Serialization
    public void saveData()
    {
        try (ObjectOutputStream empOut = new ObjectOutputStream(new FileOutputStream(employeeFile));
             ObjectOutputStream taskOut = new ObjectOutputStream(new FileOutputStream(taskFile));
             ObjectOutputStream assignmentOut = new ObjectOutputStream(new FileOutputStream(assignmentsFile))) {
            empOut.writeObject(employeesMap);
            taskOut.writeObject(tasksMap);
            assignmentOut.writeObject(employeeTasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData()
    {
        try (ObjectInputStream empIn = new ObjectInputStream(new FileInputStream(employeeFile));
             ObjectInputStream taskIn = new ObjectInputStream(new FileInputStream(taskFile));
             ObjectInputStream assignmentIn = new ObjectInputStream(new FileInputStream(assignmentsFile))) {

            employeesMap = (Map<Integer, Employee>) empIn.readObject();
            Map<Integer, Task> oldTasksMap = (Map<Integer, Task>) taskIn.readObject();
            Map<Employee, List<Task>> oldEmployeeTasks = (Map<Employee, List<Task>>) assignmentIn.readObject();

            employeeTasks = new HashMap<>();
            tasksMap = new HashMap<>();

            //Handling the task serialization between the assigned tasks and the task list so that we don't have different obects with the same values and everything else.
            for (Employee oldEmployee : oldEmployeeTasks.keySet()) {
                Employee newEmployee = employeesMap.get(oldEmployee.getIdEmployee());
                if (newEmployee != null) {
                    List<Task> existingAssignedTasks = oldEmployeeTasks.getOrDefault(newEmployee, new ArrayList<>());

                    for (Task oldTask : oldEmployeeTasks.get(oldEmployee)) {
                        if (!existingAssignedTasks.contains(oldTask)) {
                            existingAssignedTasks.add(oldTask);
                        }
                    }
                    employeeTasks.put(newEmployee, existingAssignedTasks); // Keeping the same tasks in the assigned task list
                    for(Task task: existingAssignedTasks)
                    {
                        tasksMap.putIfAbsent(task.getIdTask(), task); //Keeping the same, pottentialy assigned, tasks in the taskMap
                    }
                }
            }
            for(Task task: oldTasksMap.values()) {
                tasksMap.putIfAbsent(task.getIdTask(), task); //Putting the rest of unassigned tasks in the taskMap
            }

        } catch (IOException | ClassNotFoundException e) {
            employeesMap = new HashMap<>();
            tasksMap = new HashMap<>();
            employeeTasks = new HashMap<>();
        }
    }

}
