package org.example.GUI;

import org.example.BusinessLogic.TasksManagement;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        TasksManagement controller = new TasksManagement();

        //
//        Employee emp1 = new Employee( "Alice", 1);
//        Employee emp2 = new Employee( "Bob", 2);
//
//        SimpleTask task1 = new SimpleTask(101, 9, 12);
//        SimpleTask task2 = new SimpleTask(102, 13, 17);

//        controller.addEmployee(emp1);
//        controller.addEmployee(emp2);
//        controller.addTask(task1);
//        controller.addTask(task2);
        //Employee emp1 = new Employee( "Ale", 3);
        //controller.addEmployee(emp1);

        // Launch GUI
        TaskManagementGUI view = new TaskManagementGUI(controller);
        view.displayGUI();
    }
}