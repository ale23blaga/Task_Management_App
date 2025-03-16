package org.example.GUI;

import org.example.BusinessLogic.TasksManagement;
import org.example.BusinessLogic.Utility;
import org.example.DataModel.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.List;

public class StatisticsGUI extends JFrame {

    public StatisticsGUI(TasksManagement controller) {
        setTitle("Statistics");
        setSize(600, 900);
        setLayout(new BorderLayout());

        //Top part: tasks completed and uncompleted
        String[] columns1 = {"EmployeeId", "Name", "Completed Tasks", "Uncompleted Tasks"};
        DefaultTableModel model1 = new DefaultTableModel(columns1, 0);
        for (Employee employee : controller.getAllEmployees()){
            Map<String, Integer> statisticsForEmployee = Utility.countCompletedAndUncompletedTasks(controller.getEmployeeTasks(), employee);
            model1.addRow(new Object[]{employee.getIdEmployee(), employee.getName(),statisticsForEmployee.get("Completed"),statisticsForEmployee.get("Uncompleted")});

        }
        JPanel panel1 = new JPanel();
        JTable table1 = new JTable(model1);
        JScrollPane scrollPane1 = new JScrollPane(table1);
        JLabel label1 = new JLabel("Numer of tasks completed and uncompleted by employee:");
        panel1.add(label1, BorderLayout.NORTH);
        panel1.add(scrollPane1, BorderLayout.CENTER);



        //Bottom part: with filtering by work duration
        String[] columns2 = {"EmployeeId", "Name", "Worked Hours"};
        DefaultTableModel model2 = new DefaultTableModel(columns2, 0);
        List<Employee> filteredEmployees = Utility.getSortedEmployeesByWorkHours(controller);
        for (Employee employee : filteredEmployees){
            model2.addRow(new Object[]{employee.getIdEmployee(), employee.getName(), controller.calculateEmployeeWorkDuration(employee.getIdEmployee())});

        }
        JPanel panel2 = new JPanel();
        JTable table2 = new JTable(model2);
        JScrollPane scrollPane2 = new JScrollPane(table2);
        JLabel label2 = new JLabel("Filtered employees by work duration");
        panel2.add(label2, BorderLayout.NORTH);
        panel2.add(scrollPane2, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel1, panel2);
        splitPane.setDividerLocation(450);

        add(splitPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
