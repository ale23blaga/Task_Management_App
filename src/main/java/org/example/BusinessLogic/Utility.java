package org.example.BusinessLogic;

import org.example.DataModel.Employee;
import org.example.DataModel.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {

    //filters for work duration > 40, sort cresc dupa work durtaion, afis names
    //sortare cu map

    //met calc pt fiecare empl, nr completed & uncompleted tasks returns map

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
}
