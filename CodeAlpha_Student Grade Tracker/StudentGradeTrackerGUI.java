import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Student {
    String name;
    int marks;

    Student(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    String getGrade() {
        if (marks >= 90) return "A";
        else if (marks >= 80) return "B";
        else if (marks >= 70) return "C";
        else if (marks >= 60) return "D";
        else return "F";
    }
}

public class StudentGradeTrackerGUI extends JFrame {

    private JTextField nameField;
    private JTextField marksField;
    private JTextArea displayArea;

    private ArrayList<Student> students = new ArrayList<>();

    public StudentGradeTrackerGUI() {

        setTitle("Student Grade Tracker");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // Top Panel (Input)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        inputPanel.add(new JLabel("Student Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Marks (0-100):"));
        marksField = new JTextField();
        inputPanel.add(marksField);

        JButton addButton = new JButton("Add Student");
        inputPanel.add(addButton);

        JButton summaryButton = new JButton("Show Summary");
        inputPanel.add(summaryButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center Panel (Display)
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText();
                String marksText = marksField.getText();

                if (name.isEmpty() || marksText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields.");
                    return;
                }

                int marks;
                try {
                    marks = Integer.parseInt(marksText);
                    if (marks < 0 || marks > 100) {
                        JOptionPane.showMessageDialog(null, "Marks must be between 0 and 100.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Enter valid numeric marks.");
                    return;
                }

                students.add(new Student(name, marks));

                nameField.setText("");
                marksField.setText("");

                JOptionPane.showMessageDialog(null, "Student Added Successfully!");
            }
        });

        summaryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (students.isEmpty()) {
                    displayArea.setText("No students added yet.");
                    return;
                }

                int total = 0;
                int highest = students.get(0).marks;
                int lowest = students.get(0).marks;

                StringBuilder report = new StringBuilder();
                report.append("===== STUDENT SUMMARY =====\n\n");

                for (Student s : students) {
                    total += s.marks;

                    if (s.marks > highest)
                        highest = s.marks;

                    if (s.marks < lowest)
                        lowest = s.marks;

                    report.append("Name  : ").append(s.name).append("\n");
                    report.append("Marks : ").append(s.marks).append("\n");
                    report.append("Grade : ").append(s.getGrade()).append("\n");
                    report.append("-----------------------------\n");
                }

                double average = (double) total / students.size();

                report.append("\nClass Average : ").append(average);
                report.append("\nHighest Marks : ").append(highest);
                report.append("\nLowest Marks  : ").append(lowest);

                displayArea.setText(report.toString());
            }
        });
    }

    public static void main(String[] args) {
        new StudentGradeTrackerGUI().setVisible(true);
    }
}
