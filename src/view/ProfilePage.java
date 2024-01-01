package view;


import controller.*;
import view.organizer.OrganizerMainPage;
import view.user.UserMainPage;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Arrays;


public class ProfilePage {

    private JFrame frame;
    private UserController userController;
    private LoginController loginController;
    public String currentUsername;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ProfilePage window = new ProfilePage("temp");
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public ProfilePage(String currentUsername) {
        this.currentUsername = currentUsername;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        loginController = new LoginController(new DatabaseConnection().getConnection());
        userController = new UserController(new DatabaseConnection().getConnection());
        int userTypeInt = loginController.getAccountType(currentUsername);

        frame = new JFrame("Profile Page");
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel menuPanel = new JPanel();
        frame.getContentPane().add(menuPanel, BorderLayout.NORTH);
        menuPanel.setLayout(new GridLayout(1, 0, 0, 0));

        JPanel menuButtonPanel = new JPanel();
        menuPanel.add(menuButtonPanel);
        menuButtonPanel.setLayout(new GridLayout(0, 5, 0, 0));

        JButton homeButton = new JButton("Home");
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                if(userTypeInt == RegisterController.USER){
                    UserMainPage userMainPage = new UserMainPage(currentUsername);
                }
                else{
                    //OrganizerMainPage organizerMainPage = new (currentUsername);
                }


            }
        });
        menuButtonPanel.add(homeButton);

        JButton historyButton = new JButton("History");
        menuButtonPanel.add(historyButton);

        JButton profileButton = new JButton("Profile");
        profileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                ProfilePage profilePage = new ProfilePage(currentUsername);
            }
        });
        menuButtonPanel.add(profileButton);

        JButton logoutButton = new JButton("Logout");

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                LoginPage Lwindow = new LoginPage();
            }
        });
        menuButtonPanel.add(logoutButton);




        JButton exitButton = new JButton("Exit");
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // user status must be changed
                System.exit(0);
            }
        });
        menuButtonPanel.add(exitButton);

        JPanel menuTextPanel = new JPanel();
        menuPanel.add(menuTextPanel);
        menuTextPanel.setLayout(new GridLayout(1,0, 0, 0));

        JLabel helloLabel = new JLabel("Hello "+ currentUsername);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(helloLabel);

        JLabel balanceLabel = new JLabel("Balance: "+userController.getBalance(currentUsername)+ " TL" );
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(balanceLabel);




        JPanel centerPanel = new JPanel();
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new GridLayout(0, 1, 0, 20));

        JLabel usernameText = new JLabel("Username: " + currentUsername);
        usernameText.setFont(new Font("Tahoma", Font.BOLD, 31));
        usernameText.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(usernameText);

        JLabel balanceText = new JLabel("Balance: " + userController.getBalance(currentUsername));
        balanceText.setFont(new Font("Tahoma", Font.BOLD, 31));
        balanceText.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(balanceText);

        String userType = "Unknown";
        if (loginController.getAccountType(currentUsername) == 0){
            userType = "User";
        }
        else if (loginController.getAccountType(currentUsername) == 1){
            userType = "Organizer";
        }
        JLabel roleText = new JLabel("Role: " + userType);
        roleText.setHorizontalAlignment(SwingConstants.CENTER);
        roleText.setFont(new Font("Tahoma", Font.BOLD, 31));
        centerPanel.add(roleText);

        JPanel buttonPanel = new JPanel();
        centerPanel.add(buttonPanel);

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialog changePasswordWindowFrame = changePasswordWindow(currentUsername,frame);
                changePasswordWindowFrame.setVisible(true);


            }
        });
        changePasswordButton.setPreferredSize(new Dimension(150, 50));

        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(changePasswordButton);

        JButton changeUsernameButton = new JButton("Change Username");
        changeUsernameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDialog changeUsernameWindowFrame = changeUsernameWindow(currentUsername,frame);
                changeUsernameWindowFrame.setVisible(true);


            }
        });
        changeUsernameButton.setPreferredSize(new Dimension(150, 50));
        buttonPanel.add(changeUsernameButton);

        frame.setVisible(true);
    }

    public JDialog changePasswordWindow(String username, JFrame mainFrame) {
        JDialog changePasswordDialog= new JDialog(new JFrame(), "Change Password", true);
        changePasswordDialog.setSize(400, 200);
        changePasswordDialog.setResizable(false);
        changePasswordDialog.setLocationRelativeTo(null);
        changePasswordDialog.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        changePasswordDialog.getContentPane().add(panel);
        panel.setLayout(new GridLayout(0, 2, 10, 10));

        JLabel oldpasswordLabel = new JLabel("Old password");
        oldpasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        oldpasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        panel.add(oldpasswordLabel);

        JPasswordField oldpasswordField = new JPasswordField();
        oldpasswordField.setColumns(10);
        panel.add(oldpasswordField);

        JLabel newpasswordLabel = new JLabel("New password");
        newpasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        newpasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(newpasswordLabel);

        JPasswordField newpasswordField = new JPasswordField();
        newpasswordField.setColumns(10);
        panel.add(newpasswordField);

        JLabel confnewpasswordLabel = new JLabel("Confirm new password");
        confnewpasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        confnewpasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(confnewpasswordLabel);

        JPasswordField confnewpasswordField = new JPasswordField();
        confnewpasswordField.setColumns(10);
        panel.add(confnewpasswordField);

        JPanel panel_2 = new JPanel();
        changePasswordDialog.getContentPane().add(panel_2, BorderLayout.SOUTH);

        JButton confirmPasswordButton = new JButton("Change Password");
        confirmPasswordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                char[] newPass1 = newpasswordField.getPassword();
                char[] newPass2 = confnewpasswordField.getPassword();
                String oldPassword = new String(oldpasswordField.getPassword());
                if (oldPassword.equals(userController.getUserByUsername(username).getPassword())){
                    if (Arrays.equals(newPass1, newPass2)){
                        if(userController.changePassword(username,new String(newPass1))){
                            JOptionPane.showMessageDialog(new JFrame(), "Password is changed successfully");
                            changePasswordDialog.dispose();
                            mainFrame.dispose();
                            new ProfilePage(username);
                        }
                        else{
                            JOptionPane.showMessageDialog(new JFrame(), "Requirements not met!!!");
                        }

                    }else{
                        JOptionPane.showMessageDialog(new JFrame(), "Passwords not matching!!!");
                    }

                }
                else{
                    JOptionPane.showMessageDialog(new JFrame(), "Old password is wrong!!!");
                }



            }
        });
        panel_2.add(confirmPasswordButton);

        return changePasswordDialog;

    }

    public JDialog changeUsernameWindow(String username, JFrame mainFrame) {
        JDialog changeUsernameDialog= new JDialog(new JFrame(), "Change Username", true);
        changeUsernameDialog.setSize(400, 200);
        changeUsernameDialog.setResizable(false);
        changeUsernameDialog.setLocationRelativeTo(null);
        changeUsernameDialog.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        changeUsernameDialog.getContentPane().add(panel);
        panel.setLayout(new GridLayout(0, 2, 10, 10));

        JLabel oldpasswordLabel = new JLabel("Password");
        oldpasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        oldpasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        panel.add(oldpasswordLabel);

        JPasswordField oldpasswordField = new JPasswordField();
        oldpasswordField.setColumns(10);
        panel.add(oldpasswordField);

        JLabel newUsernameLabel = new JLabel("New username");
        newUsernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        newUsernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(newUsernameLabel);

        JTextField newUsernameField = new JTextField();
        newUsernameField.setColumns(10);
        panel.add(newUsernameField);

        JLabel confnewUsernameLabel = new JLabel("Confirm new username");
        confnewUsernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        confnewUsernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(confnewUsernameLabel);

        JTextField confnewusernameField = new JTextField();
        confnewusernameField.setColumns(10);
        panel.add(confnewusernameField);

        JPanel panel_2 = new JPanel();
        changeUsernameDialog.getContentPane().add(panel_2, BorderLayout.SOUTH);

        JButton confirmUsernameButton = new JButton("Change Username");
        confirmUsernameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String newUsername1 = newUsernameField.getText();
                String newUsername2 = confnewusernameField.getText();
                String oldPassword = new String(oldpasswordField.getPassword());
                if (oldPassword.equals(userController.getUserByUsername(username).getPassword())){
                    if ((newUsername1.equals(newUsername2)) ){
                        if(userController.changeUsername(username,newUsername1)){
                            JOptionPane.showMessageDialog(new JFrame(), "Username is changed successfully");
                            changeUsernameDialog.dispose();
                            mainFrame.dispose();
                            new ProfilePage(newUsername1);
                        }
                        else{
                            JOptionPane.showMessageDialog(new JFrame(), "Requirements not met!!!");
                        }

                    }else{
                        JOptionPane.showMessageDialog(new JFrame(), "Usernames not matching!!!");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(new JFrame(), "Password is wrong!!!");
                }




            }
        });
        panel_2.add(confirmUsernameButton);

        return changeUsernameDialog;

    }
}
