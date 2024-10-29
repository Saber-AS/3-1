import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class AddressBookClientGUI {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private Map<String, String> addressBook = new HashMap<>();
    private JTextArea displayArea;
    private JTextField nameField, phoneField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddressBookClientGUI::new);
    }

    public AddressBookClientGUI() {
        JFrame frame = new JFrame("个人通讯录系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("姓名:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("电话:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        JButton addButton = new JButton("添加");
        addButton.addActionListener(e -> addContact());
        inputPanel.add(addButton);

        JButton getButton = new JButton("查询");
        getButton.addActionListener(e -> getContact());
        inputPanel.add(getButton);

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> deleteContact());
        inputPanel.add(deleteButton);

        JButton updateButton = new JButton("更新");
        updateButton.addActionListener(e -> updateContact());
        inputPanel.add(updateButton);

        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        if (name.isEmpty() || phone.isEmpty()) {
            displayArea.append("姓名和电话不能为空。\n");
            return;
        }
        String response = sendCommand("ADD", name, phone);
        displayArea.append(response + "\n");
        nameField.setText("");
        phoneField.setText("");
    }

    private void getContact() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            displayArea.append("姓名不能为空。\n");
            return;
        }
        String response = sendCommand("GET", name);
        displayArea.append("查询结果: " + response + "\n");
        nameField.setText("");
    }

    private void deleteContact() {
        String name = nameField.getText();
        if (name.isEmpty()) {
            displayArea.append("姓名不能为空。\n");
            return;
        }
        String response = sendCommand("DELETE", name);
        displayArea.append(response + "\n");
        nameField.setText("");
    }

    private void updateContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        if (name.isEmpty() || phone.isEmpty()) {
            displayArea.append("姓名和电话不能为空。\n");
            return;
        }
        String response = sendCommand("UPDATE", name, phone);
        displayArea.append(response + "\n");
        nameField.setText("");
        phoneField.setText("");
    }

    private String sendCommand(String command, String... args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            output.writeObject(command);
            for (String arg : args) {
                output.writeObject(arg);
            }
            return (String) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "操作失败: " + e.getMessage();
        }
    }
}
