package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import controller.*;
import view.organizer.OrganizerMainPage;
import view.user.UserMainPage;

public class RegisterPage extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RegisterPage registerPage = new RegisterPage();
                    registerPage.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public RegisterPage() {
        initialize();
    }

    private void initialize() {
        setResizable(false);
        setBackground(Color.WHITE);
        setTitle("Register Page");
        setBounds(100, 100, 500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Ticket Management System Register");
        headerLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JPanel panelNorth = new JPanel();
        panelNorth.add(headerLabel);
        contentPane.add(panelNorth, BorderLayout.NORTH);

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BorderLayout());

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(3, 1));
        fieldsPanel.add(new JLabel("Username:"));
        fieldsPanel.add(new JTextField(20));

        fieldsPanel.add(new JLabel("Password:"));
        fieldsPanel.add(new JPasswordField());

        fieldsPanel.add(new JLabel("User Type:"));
        String[] userTypeOptions = {"Organizer", "User"};
        JComboBox<String> userTypeComboBox = new JComboBox<>(userTypeOptions);
        fieldsPanel.add(userTypeComboBox);

        panelCenter.add(fieldsPanel, BorderLayout.CENTER);

        contentPane.add(panelCenter, BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userType = userTypeComboBox.getSelectedItem().toString();;
                String usernameText = ((JTextField) fieldsPanel.getComponent(1)).getText();
                char[] passwordChars = ((JPasswordField) fieldsPanel.getComponent(3)).getPassword();
                String passwordText = new String(passwordChars);
                if (userType.equals("User")){
                    if (new RegisterController(new DatabaseConnection().getConnection()).register(usernameText,passwordText,RegisterController.USER)){
                        dispose();
                        LoginPage loginPage = new LoginPage();
                    }
                }
                else{
                    if(new RegisterController(new DatabaseConnection().getConnection()).register(usernameText,passwordText,RegisterController.ORGANIZER)){
                        dispose();
                        LoginPage loginPage = new LoginPage();
                    }
                }

            }

        });
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                try {
                    LoginPage loginPage = new LoginPage();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        panelSouth.add(registerButton);
        panelSouth.add(cancelButton);

        contentPane.add(panelSouth, BorderLayout.SOUTH);

        setVisible(true);
    }
}
