package org.example.GUI;

import org.example.BusinessLogic.TasksManagement;
import org.example.DataModel.ComplexTask;
import org.example.DataModel.Employee;
import org.example.DataModel.SimpleTask;
import org.example.DataModel.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TaskManagementGUI extends JFrame {
    private TasksManagement controller;
    private JTable employeeTable, taskTable;
    private JButton addEmployeeButton, addTaskButton, loadButton, assignButton, showEmployeeButton, showTaskButton, modifyTaskStatus;

    public TaskManagementGUI(TasksManagement controller) {
        this.controller = controller;
        setTitle("Task Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        addEmployeeButton = new JButton("Add Employee");
        addTaskButton = new JButton("Add Task");
        assignButton = new JButton("Assign Task");
        loadButton = new JButton("Load");
        modifyTaskStatus = new JButton("Modify Task");
        topPanel.add(addEmployeeButton);
        topPanel.add(addTaskButton);
        topPanel.add(assignButton);
        topPanel.add(loadButton);
        topPanel.add(modifyTaskStatus);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        employeeTable = new JTable();
        taskTable = new JTable();
        centerPanel.add(new JScrollPane(employeeTable));
        centerPanel.add(new JScrollPane(taskTable));
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        showEmployeeButton = new JButton("Show Employee");
        showTaskButton = new JButton("Show Task");
        bottomPanel.add(showEmployeeButton);
        bottomPanel.add(showTaskButton);
        bottomPanel.add(loadButton);
        add(bottomPanel, BorderLayout.SOUTH);

        updateTables();

        loadButton.addActionListener(e -> updateTables());
        addEmployeeButton.addActionListener(e-> showAddEmployeeDialog());
        addTaskButton.addActionListener(e -> showAddTaskDialog());
        assignButton.addActionListener(e -> assigningTaskToEmployee());
        modifyTaskStatus.addActionListener(e-> modifyingTaskStatus());
        showEmployeeButton.addActionListener(e -> showEmployeeDetails());
        showTaskButton.addActionListener(e -> showTaskDetails());
    }

    private void updateTables() {
        String[] employeeColumns = {"ID", "Name"};
        DefaultTableModel employeeModel = new DefaultTableModel(employeeColumns, 0);
        for (Employee e : controller.getAllEmployees()) {
            employeeModel.addRow(new Object[]{e.getIdEmployee(), e.getName()});
        }
        employeeTable.setModel(employeeModel);

        String[] taskColumns = {"ID", "Status", "Taks Type"};
        DefaultTableModel taskModel = new DefaultTableModel(taskColumns, 0);
        for (Task t : controller.getAllTasks()) {
            taskModel.addRow(new Object[]{t.getIdTask(),  t.getStatusTask(), t.getClass().getSimpleName()});
        }
        taskTable.setModel(taskModel);
    }

    private void showAddEmployeeDialog() {
        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JPanel panel = new JPanel();
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        int restul = JOptionPane.showConfirmDialog(null, panel, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
        if(restul == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            controller.addEmployee(new Employee(name, id));
            updateTables();
        }
    }

    private void showAddTaskDialog() {
        JTextField idField = new JTextField(10);
        JTextField startHourField = new JTextField(10);
        JTextField endHourField = new JTextField(10);
        String[] taskType = {"Simple Task", "Complex Task"};
        JComboBox<String> taskTypeComboBox = new JComboBox<>(taskType);

        JPanel panel = new JPanel();
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Task Type:"));
        panel.add(taskTypeComboBox);
        panel.add(new JLabel("Start Hour:"));
        panel.add(startHourField);
        panel.add(new JLabel("End Hour:"));
        panel.add(endHourField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            String selectedTaskType = (String) taskTypeComboBox.getSelectedItem();
            int startHour = Integer.parseInt(startHourField.getText());
            int endHour = Integer.parseInt(endHourField.getText());
            if (selectedTaskType.equals("Simple Task")) {
                controller.addTask(new SimpleTask(id, startHour, endHour));
            }
            else{
                controller.addTask(new ComplexTask(id));
            }
            updateTables();
        }
    }

    private void assigningTaskToEmployee() {
        int selectedEmployeeRow = employeeTable.getSelectedRow();
        int selectedTaskRow = taskTable.getSelectedRow();
        if (selectedEmployeeRow == -1 || selectedTaskRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee and a task.");
            return;
        }
        int employeeId = (int) employeeTable.getValueAt(selectedEmployeeRow, 0);
        int taskId = (int) taskTable.getValueAt(selectedTaskRow, 0);
        controller.assignTaskToEmployee(employeeId, controller.getTaskById(taskId));
        JOptionPane.showMessageDialog(this, "Task assigned successfully.");
    }

    private void modifyingTaskStatus() {
        int selectedEmployeeRow = employeeTable.getSelectedRow();
        int selectedTaskRow = taskTable.getSelectedRow();
        if(selectedEmployeeRow == -1 || selectedTaskRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee and a task.");
            return;
        }
        int employeeId = (int) employeeTable.getValueAt(selectedEmployeeRow, 0);
        int taskId = (int) taskTable.getValueAt(selectedTaskRow, 0);
        controller.modifyTaskStatus(employeeId, taskId);
        JOptionPane.showMessageDialog(this, "Task status modified successfully.");
        updateTables();
    }

    private void showEmployeeDetails() {
        int selectedRow = employeeTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee.");
            return;
        }

        int employeeId = (int) employeeTable.getValueAt(selectedRow, 0);
        Employee selectedEmployee = controller.getEmployeeById(employeeId);
        new EmployeeDetailsGUI(selectedEmployee, controller);
    }

    private void showTaskDetails()
    {
        int selectedRow = taskTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task.");
            return;
        }

        int taskId = (int) taskTable.getValueAt(selectedRow, 0);
        Task task = controller.getTaskById(taskId);
        new TaskDetailsGUI(task, controller);
    }

    public void displayGUI() { setVisible(true); }

}
