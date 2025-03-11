package org.example.BusinessLogic;

import org.example.DataModel.ComplexTask;
import org.example.DataModel.Employee;
import org.example.DataModel.SimpleTask;
import org.example.DataModel.Task;

import java.util.*;

public class TasksManagement {
    private Map<Employee, List<Task>> map;

    public TasksManagement() {
        map = new HashMap<>();
    }

    public void assignTaskToEmployee(int idEmployee, Task task){
        //cautam dupa employee si ii mai punem task ul in lista
        List<Task> employeesList = map.get(idEmployee);
        employeesList.add(task);
    }

    public int calculateEmployeeWorkDuration(int idEmployee){
        int total = 0;
        List<Task> employeesTaskList = map.get(idEmployee);
        // sa pui eroare daca nu exista employee in map
        total = calculateTaskWorkDuration(employeesTaskList);
        return total;
    }

    public void modifyTaskStatus(int idEmployee, int idTask){
        List<Task> employeesTaskList = map.get(idEmployee);
        // sa pui eroare daca nu exista employee in map
        changeTaskStatus(employeesTaskList, idTask); // schimba statusul la un task dintr-o lista
    }



    // functiile de mai jos presupun ca ar trebui sa mearga in utility sau ceva
    private void changeTaskStatus(List<Task> tasks, int idTask)
    {
        for (Task task : tasks) {
            //daca am ajuns la task-ul dorit
            if (task.getIdTask() == idTask) { // aici ajunge si daca e task simplu si daca e task complex

                //asta o poti face functie separata numita change task status with opposite
                if (task.getStatusTask().equals("Completed"))
                    task.setStatusTask("Uncompleted");
                else {
                    // nu ar trebui schimbata intreaga lista cu completed la complex?
                    task.setStatusTask("Completed");

                }
            }
            else // daca nu ne uitam ori mai jos in taskul complex, ori prin cele simple
            if (task instanceof ComplexTask) {
                changeTaskStatus(((ComplexTask) task).getTasks(), idTask);
            }
        }
    }

    private int calculateTaskWorkDuration(List<Task> tasks)
    {
        int total =  0;
        for( Task task : tasks )
        {
            if (task instanceof ComplexTask)
            {
                total += calculateTaskWorkDuration(((ComplexTask) task).getTasks());
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

}
