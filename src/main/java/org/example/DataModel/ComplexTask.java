package org.example.DataModel;

import java.util.*;

public class ComplexTask extends Task {

    List<Task> tasks = new ArrayList<>();

    public ComplexTask(int idTask, String statusTask) {
        super(idTask, statusTask);
    }

    public int estimateDuration(){
        return 0;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void deleteTask(Task task){
        tasks.remove(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
