package org.example.DataAccess;

import org.example.DataModel.ComplexTask;
import org.example.DataModel.Employee;
import org.example.DataModel.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataS {

    private static final String employeeFile = "employees.ser";
    private static final String taskFile = "tasks.ser";
    private static final String assignmentsFile = "assignments.ser";

    //Serialization

    //Save inside the file
    public static void saveData(Map<Integer, Employee> employeesMap, Map<Integer, Task> tasksMap, Map<Employee, List<Task>> employeeTasks)
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

    //Load from the file
    public static void loadData(Map<Integer, Employee> employeesMap, Map<Integer, Task> tasksMap, Map<Employee, List<Task>> employeeTasks) //fa partea de taskuri complexe
    {
        try (ObjectInputStream empIn = new ObjectInputStream(new FileInputStream(employeeFile));
             ObjectInputStream taskIn = new ObjectInputStream(new FileInputStream(taskFile));
             ObjectInputStream assignmentIn = new ObjectInputStream(new FileInputStream(assignmentsFile))) {

            Map<Integer, Employee> loadedEmployees = (Map<Integer, Employee>) empIn.readObject();
            Map<Integer, Task> oldTasksMap = (Map<Integer, Task>) taskIn.readObject();
            Map<Employee, List<Task>> oldEmployeeTasks = (Map<Employee, List<Task>>) assignmentIn.readObject();

            employeesMap.clear();
            tasksMap.clear();
            employeeTasks.clear();

            if (loadedEmployees != null) employeesMap.putAll(loadedEmployees);
            if (oldTasksMap == null) oldTasksMap = new HashMap<>();
            if (oldEmployeeTasks == null) oldEmployeeTasks = new HashMap<>();

            //Handling the task serialization between the assigned tasks and the task list so that we don't have different objects with the same values and everything else.
            for (Employee oldEmployee : oldEmployeeTasks.keySet()) {
                Employee newEmployee = employeesMap.get(oldEmployee.getIdEmployee()); //corelating the employees from the employee map with the ones from employeeTask map
                if (newEmployee != null) {
                    List<Task> existingAssignedTasks = employeeTasks.computeIfAbsent(newEmployee, k -> new ArrayList<>());

                    for (Task oldTask : oldEmployeeTasks.get(oldEmployee)) {
                        if (oldTask != null && !existingAssignedTasks.contains(oldTask)) {
                            existingAssignedTasks.add(oldTask);
                        }
                    }
                    for (Task task : existingAssignedTasks) {
                        if (task != null) {
                            tasksMap.putIfAbsent(task.getIdTask(), task); //Keeping the same assigned tasks in the taskMap
                        }
                    }
                }
            }

            //Putting the rest of unassigned tasks in the taskMap
            for (Task task : oldTasksMap.values()) {
                if (task != null) {
                    tasksMap.putIfAbsent(task.getIdTask(), task);
                }
            }

            //Fixing the Sub Tasks References inside the list that is in Complex Task
            for (Task task : tasksMap.values()) {
                if (task instanceof ComplexTask) {
                    ComplexTask complexTask = (ComplexTask) task;
                    List<Task> updatedSubTasks = new ArrayList<>();
                    for (Task subTask : complexTask.getTasks()) {
                        if (subTask != null) {
                            Task newSubTask = tasksMap.get(subTask.getIdTask());
                            if (newSubTask != null) {
                                updatedSubTasks.add(newSubTask);
                            }
                        }
                    }
                    complexTask.setTasks(updatedSubTasks);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            employeesMap.clear();
            tasksMap.clear();
            employeeTasks.clear();
        }
    }



}
