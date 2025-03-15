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

    //trebe sa fie incarcat pe git
    // sa fie ok denumirea sa fie acces dat la utilizator
    //cele 3 diagrame in draw.io si png - sa fie toate
    // punctajul facem de 5 ce e obliatoriu
    //facem in plus ce mai avem vreme timp si chef
    //nota - 1 2 puncte maxim
    // variabile denumite prost si gresit ! sa nu fie romana si engleza
    // sa respectam conventiile de denumire
    //sa incercam sa scriem clasele cat de concise
    //daca avem o metoda lunga o putem sparte in mai multe sa nu avem met de 100 de linii
    //nu e gresit da se putea face mai usor
}
