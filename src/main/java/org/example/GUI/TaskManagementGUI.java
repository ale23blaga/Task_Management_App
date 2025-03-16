package org.example.GUI;

import org.example.BusinessLogic.TasksManagement;
import org.example.DataAccess.DataS;
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
    private JButton addEmployeeButton, addTaskButton, loadButton, assignButton, showEmployeeButton, showTaskButton, modifyTaskStatusButton, addSubTaskButton;
    private JButton viewStatisticsButton;

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
        addSubTaskButton = new JButton("Add Sub Tasks");
        modifyTaskStatusButton = new JButton("Modify Task");
        topPanel.add(addEmployeeButton);
        topPanel.add(addTaskButton);
        topPanel.add(assignButton);
        topPanel.add(addSubTaskButton);
        topPanel.add(modifyTaskStatusButton);
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
        viewStatisticsButton = new JButton("View Statistics");
        loadButton = new JButton("Load");
        bottomPanel.add(showEmployeeButton);
        bottomPanel.add(showTaskButton);
        bottomPanel.add(viewStatisticsButton);
        bottomPanel.add(loadButton);
        add(bottomPanel, BorderLayout.SOUTH);

        updateTables();

        loadButton.addActionListener(e -> updateTables());
        addEmployeeButton.addActionListener(e-> showAddEmployeeDialog());
        addTaskButton.addActionListener(e -> showAddTaskDialog());
        assignButton.addActionListener(e -> assigningTaskToEmployee());
        addSubTaskButton.addActionListener(e -> showAddSubTaskDialog());
        modifyTaskStatusButton.addActionListener(e-> modifyingTaskStatus());
        showEmployeeButton.addActionListener(e -> showEmployeeDetails());
        showTaskButton.addActionListener(e -> showTaskDetails());
        viewStatisticsButton.addActionListener(e -> viewStatistics());
    }

    private void updateTables() {
        String[] employeeColumns = {"ID", "Name"};
        DefaultTableModel employeeModel = new DefaultTableModel(employeeColumns, 0);
        for (Employee employee : controller.getAllEmployees()) {
            employeeModel.addRow(new Object[]{employee.getIdEmployee(), employee.getName()});
        }
        employeeTable.setModel(employeeModel);

        String[] taskColumns = {"ID", "Status", "Taks Type", "Estimate Work Duration"};
        DefaultTableModel taskModel = new DefaultTableModel(taskColumns, 0);
        for (Task task : controller.getAllTasks()) {
            if (task instanceof SimpleTask) {
                taskModel.addRow(new Object[]{task.getIdTask(),  task.getStatusTask(), task.getClass().getSimpleName(), ((SimpleTask)task).estimateDuration()});
            }else {
                taskModel.addRow(new Object[]{task.getIdTask(),  task.getStatusTask(), task.getClass().getSimpleName(), ((ComplexTask)task).estimateDuration()});
            }

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

        //Extra fields for simple Tasks.
        panel.add(new JLabel("Start Hour:"));
        panel.add(startHourField);
        panel.add(new JLabel("End Hour:"));
        panel.add(endHourField);

        //Adding and removing the simple task specific fields.
        taskTypeComboBox.addActionListener(e -> {
            if (taskTypeComboBox.getSelectedItem().equals("Complex Task")) {
                panel.remove(endHourField);
                panel.remove(startHourField);
                panel.remove(panel.getComponentCount() - 1);
                panel.remove(panel.getComponentCount() - 1);
                panel.revalidate();
                panel.repaint();
            } else {
                if(!panel.isAncestorOf(startHourField)) {
                    panel.add(new JLabel("Start Hour:"));
                    panel.add(startHourField);
                    panel.add(new JLabel("End Hour:"));
                    panel.add(endHourField);
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });

        taskTypeComboBox.setSelectedItem(0);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            String selectedTaskType = (String) taskTypeComboBox.getSelectedItem();
            if (selectedTaskType.equals("Simple Task")) {
                int startHour = Integer.parseInt(startHourField.getText());
                int endHour = Integer.parseInt(endHourField.getText());
                controller.addTask(new SimpleTask(id, startHour, endHour));
            }
            else{
                controller.addTask(new ComplexTask(id));
            }
            updateTables();
        }
    }

    private void showAddSubTaskDialog() {
        int selectedComplexTaskRow = taskTable.getSelectedRow();
        if(selectedComplexTaskRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a complex task");
            return;
        }

        int complexTaskId = (int) taskTable.getValueAt(selectedComplexTaskRow, 0);
        Task complexTask = controller.getTaskById(complexTaskId);
        if ( !(complexTask instanceof ComplexTask) ) {
            JOptionPane.showMessageDialog(this, "Please select a COMPLEX task");
            return;
        }

        JTextField idField = new JTextField(10);
        JPanel panel = new JPanel();
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Add Sub-Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            if (id == complexTaskId) {
                JOptionPane.showMessageDialog(this, "Cannot assign a complex task to be it's own sub-task.");

            }
            else {
                Task subTask = controller.getTaskById(id);
                if(((ComplexTask) complexTask).getTasks().contains(subTask)){
                    JOptionPane.showMessageDialog(this, "Cannot assign the same sub task again.");
                    return;
                }
                ((ComplexTask) complexTask).addTask(subTask);
                JOptionPane.showMessageDialog(this, "Sub Task added successfully.");
            }
        }
        controller.saveDataFromTaskManagement();
        updateTables();
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
        if (controller.getTasksByEmployeeId(employeeId).contains(controller.getTaskById(taskId))) {
            JOptionPane.showMessageDialog(this, "Task already assigned to the employee.");
            return;
        }
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

    private void showTaskDetails() {
        int selectedRow = taskTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task.");
            return;
        }

        int taskId = (int) taskTable.getValueAt(selectedRow, 0);
        Task task = controller.getTaskById(taskId);
        new TaskDetailsGUI(task, controller);
    }

    private void viewStatistics() {
        new StatisticsGUI(controller);
    }

    public void displayGUI() { setVisible(true); }

}
