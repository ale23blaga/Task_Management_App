package org.example.GUI;

import org.example.BusinessLogic.TasksManagement;
import org.example.DataModel.ComplexTask;
import org.example.DataModel.SimpleTask;
import org.example.DataModel.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TaskDetailsGUI extends JFrame {
    private Task task;
    private TasksManagement controller;

    public TaskDetailsGUI(Task task, TasksManagement controller) {
        this.task = task;
        this.controller = controller;

        setTitle("Task Details: " + task.getIdTask());
        setSize(800, 600);
        setLayout(new BorderLayout());

        //Task info
        JPanel infoPanel = new JPanel();
        infoPanel.add(new JLabel("Task ID: " + task.getIdTask()));
        infoPanel.add(new JLabel( "Task Type: " + task.getClass().getSimpleName()));
        infoPanel.add(new JLabel("Task Status: " + task.getStatusTask()));
        add(infoPanel, BorderLayout.NORTH);

        if(task instanceof SimpleTask)
        {
            JPanel simpleTaskInfoPanel = new JPanel();
            simpleTaskInfoPanel.add(new JLabel("Starting hour:" + ((SimpleTask)task).getStartHour()));
            simpleTaskInfoPanel.add(new JLabel("Ending hour:" + ((SimpleTask)task).getEndHour()));
            add(simpleTaskInfoPanel, BorderLayout.CENTER);
        }
        else
        {
            String[] columns = {"Task ID", "Task Type", "Task Status"};
            DefaultTableModel taskTableModel = new DefaultTableModel(columns, 0);
            List<Task> subTasks = ((ComplexTask)task).getTasks();
            for(Task subTask : subTasks)
            {
                taskTableModel.addRow(new Object[]{task.getIdTask(), task.getClass().getSimpleName(), task.getStatusTask()});
            }
            JTable table = new JTable(taskTableModel);
            add(new JLabel("SubTasks:"), BorderLayout.CENTER);
            add(new JScrollPane(table), BorderLayout.SOUTH);
        }
        setVisible(true);
    }
}
