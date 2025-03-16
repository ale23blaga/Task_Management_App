package org.example.DataModel;

import java.io.Serializable;
import java.util.Objects;

sealed abstract public class Task implements Serializable permits SimpleTask, ComplexTask{
    private int idTask;
    private String statusTask;

    public Task(int idTask) {
        this.idTask = idTask;
        this.statusTask = "Uncompleted";
    }

    abstract int estimateDuration();

    public int getIdTask() {
        return idTask;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public void modifyStatus() {
        if (this.getStatusTask().equals("Completed"))
            this.statusTask = "Uncompleted";
        else this.statusTask = "Completed";
    }

    //For serialization
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return idTask == task.idTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTask);
    }
}
