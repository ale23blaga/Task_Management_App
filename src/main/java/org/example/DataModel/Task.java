package org.example.DataModel;

abstract public class Task implements Comparable<Task> {
    private int idTask;
    private String statusTask;

    abstract int estimateDuration();

    public Task(int idTask, String statusTask) {
        this.idTask = idTask;
        this.statusTask = statusTask;
    }

    public int getIdTask() {
        return idTask;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
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
