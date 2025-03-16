package org.example.DataModel;

import java.util.*;

public non-sealed class ComplexTask extends Task {

    private List<Task> subTasks = new ArrayList<>();

    public ComplexTask(int idTask) {
        super(idTask);
    }

    @Override
    public int estimateDuration(){
        if (subTasks.isEmpty())
            return 0;
        return subTasks.stream().mapToInt(Task::estimateDuration).sum();
    }

    public void addTask(Task task){
        subTasks.add(task);
    }

    public void deleteTask(Task task){
        subTasks.remove(task);
    }

    public List<Task> getTasks() {
        return subTasks;
    }

    public void setTasks(List<Task> tasks) {
        this.subTasks = tasks;
    }
}
