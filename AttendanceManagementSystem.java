import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class AttendanceManagementSystem extends Frame {

    private Map<String, Boolean> attendanceMap;
    private Map<String, String> absentMessages;

    public AttendanceManagementSystem() {
        super("Attendance Management System");

        attendanceMap = new HashMap<>();
        absentMessages = new HashMap<>();

        initializeUI();

        setSize(600, 400);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        TextArea displayArea = new TextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(Color.LIGHT_GRAY);

        Panel buttonPanel = new Panel(new FlowLayout());

        Button markAttendanceButton = new Button("Mark Attendance");
        markAttendanceButton.setBackground(Color.GREEN);
        markAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAttendance(displayArea);
            }
        });

        Button markAbsentButton = new Button("Mark Absent");
        markAbsentButton.setBackground(Color.RED);
        markAbsentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markAbsent(displayArea);
            }
        });

        Button clearAttendanceButton = new Button("Clear Attendance");
        clearAttendanceButton.setBackground(Color.YELLOW);
        clearAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAttendance(displayArea);
            }
        });

        Button viewAttendanceButton = new Button("View Attendance");
        viewAttendanceButton.setBackground(Color.CYAN);
        viewAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAttendance(displayArea);
            }
        });

        Button sendMessageButton = new Button("Send Message to Absent");
        sendMessageButton.setBackground(Color.ORANGE);
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToAbsent(displayArea);
            }
        });

        Button generateReceiptButton = new Button("Generate Receipt");
        generateReceiptButton.setBackground(Color.MAGENTA);
        generateReceiptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReceipt(displayArea);
            }
        });

        buttonPanel.add(markAttendanceButton);
        buttonPanel.add(markAbsentButton);
        buttonPanel.add(clearAttendanceButton);
        buttonPanel.add(viewAttendanceButton);
        buttonPanel.add(sendMessageButton);
        buttonPanel.add(generateReceiptButton);

        add(displayArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void markAttendance(TextArea displayArea) {
        String studentRollNumber = JOptionPane.showInputDialog(this, "Enter student roll number:");
        if (studentRollNumber != null && !studentRollNumber.isEmpty()) {
            attendanceMap.put(studentRollNumber, true);
            displayArea.append("Roll number " + studentRollNumber + " is marked present.\n");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid student roll number.");
        }
    }

    private void markAbsent(TextArea displayArea) {
        String studentRollNumber = JOptionPane.showInputDialog(this, "Enter student roll number:");
        if (studentRollNumber != null && !studentRollNumber.isEmpty()) {
            attendanceMap.put(studentRollNumber, false);
            displayArea.append("Roll number " + studentRollNumber + " is marked absent.\n");
            String absentMessage = JOptionPane.showInputDialog(this, "Enter message for absent student:");
            if (absentMessage != null && !absentMessage.isEmpty()) {
                absentMessages.put(studentRollNumber, absentMessage);
                displayArea.append("Message for Roll number " + studentRollNumber + ": " + absentMessage + "\n");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid student roll number.");
        }
    }

    private void clearAttendance(TextArea displayArea) {
        attendanceMap.clear();
        absentMessages.clear();
        displayArea.setText("Attendance has been cleared.\n");
    }

    private void viewAttendance(TextArea displayArea) {
        if (attendanceMap.isEmpty()) {
            displayArea.setText("No attendance recorded yet.\n");
        } else {
            int presentCount = 0;
            int absentCount = 0;
            displayArea.setText("Attendance:\n");
            for (Map.Entry<String, Boolean> entry : attendanceMap.entrySet()) {
                String status = entry.getValue() ? "Present" : "Absent";
                displayArea.append("Roll number " + entry.getKey() + ": " + status + "\n");
                if (entry.getValue()) {
                    presentCount++;
                } else {
                    absentCount++;
                }
            }
            displayArea.append("\nStatistics:\n");
            displayArea.append("Present: " + presentCount + "\n");
            displayArea.append("Absent: " + absentCount + "\n");
        }
    }

    private void sendMessageToAbsent(TextArea displayArea) {
        if (absentMessages.isEmpty()) {
            displayArea.append("No absent students to send messages to.\n");
        } else {
            StringBuilder messageBuilder = new StringBuilder("Messages for absent students:\n");
            for (Map.Entry<String, String> entry : absentMessages.entrySet()) {
                messageBuilder.append("Roll number " + entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            displayArea.append(messageBuilder.toString());
        }
    }

    private void generateReceipt(TextArea displayArea) {
        String studentRollNumber = JOptionPane.showInputDialog(this, "Enter student roll number:");
        if (studentRollNumber != null && !studentRollNumber.isEmpty()) {
            Boolean status = attendanceMap.get(studentRollNumber);
            String message = absentMessages.get(studentRollNumber);
            if (status != null) {
                String receipt = "Receipt for Roll Number " + studentRollNumber + "\n";
                receipt += "Status: " + (status ? "Present" : "Absent") + "\n";
                if (status != null && !status) {
                    receipt += "Absent Message: " + (message != null ? message : "No message provided") + "\n";
                }
                displayArea.append(receipt + "\n");
                saveReceiptToFile(studentRollNumber, receipt);
            } else {
                JOptionPane.showMessageDialog(this, "No record found for this roll number.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid student roll number.");
        }
    }

    private void saveReceiptToFile(String rollNumber, String receipt) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Receipt_" + rollNumber + ".txt"))) {
            writer.write(receipt);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving receipt: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AttendanceManagementSystem();
    }
}
