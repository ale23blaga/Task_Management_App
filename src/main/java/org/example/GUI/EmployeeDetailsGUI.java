package org.example.GUI;

import org.example.BusinessLogic.TasksManagement;
import org.example.BusinessLogic.Utility;
import org.example.DataModel.Employee;
import org.example.DataModel.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class EmployeeDetailsGUI extends JFrame {
    private Employee employee;
    private TasksManagement controller;

    public EmployeeDetailsGUI(Employee employee, TasksManagement controller) {
        this.employee = employee;
        this.controller = controller;

        setTitle("Employee Details:" + employee.getName());

        setSize(800, 600);
        setLayout(new BorderLayout());

        //Employee Information
        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel("Id: " + employee.getIdEmployee()));
        infoPanel.add(new JLabel("Name: " + employee.getName()));
        add(infoPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Task Type", "Status"};
        DefaultTableModel taskTableModel = new DefaultTableModel(columns, 0);
        List<Task> tasks = controller.getTasksByEmployeeId(employee.getIdEmployee());
        for (Task task : tasks) {
            taskTableModel.addRow(new Object[]{task.getIdTask(), task.getClass().getSimpleName(), task.getStatusTask() });
        }

        JTable taskTable = new JTable(taskTableModel);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);

        //completed and uncompleted task count
        Map<String, Integer> tasksCount = Utility.countCompletedAndUncompletedTasks(controller.getEmployeeTasks(), employee);
        JPanel taskStatsPanel = new JPanel();
        taskStatsPanel.add(new JLabel("Task Count"));
        taskStatsPanel.add(new JLabel("Completed: " + tasksCount.get("Completed")));
        taskStatsPanel.add(new JLabel("Uncompleted: " + tasksCount.get("Uncompleted")));
        add(taskStatsPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

}
