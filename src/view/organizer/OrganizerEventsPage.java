package view.organizer;

import controller.DatabaseConnection;
import controller.EventController;
import controller.OrganizerController;
import view.LoginPage;
import view.ProfilePage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class OrganizerEventsPage extends JFrame {

    private JFrame frame;

    public String currentUsername;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OrganizerEventsPage window = new OrganizerEventsPage("Temp");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public OrganizerEventsPage(String currentUsername) {
        this.currentUsername = currentUsername;
        initialize();
    }

    private void initialize() {
        OrganizerController organizerController = new OrganizerController(new DatabaseConnection().getConnection());
        frame = new JFrame("Organizer Events Page");
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
                OrganizerMainPage userMainPage = new OrganizerMainPage(currentUsername);
            }
        });
        menuButtonPanel.add(homeButton);

        JButton eventsButton = new JButton("Events");

        eventsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                OrganizerEventsPage organizerEventsPage = new OrganizerEventsPage(currentUsername);
            }
        });

        menuButtonPanel.add(eventsButton);

        JButton profileButton = new JButton("Profile");
        profileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                ProfilePage Lwindow = new ProfilePage(currentUsername);
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
                System.exit(0);
            }
        });
        menuButtonPanel.add(exitButton);

        JPanel menuTextPanel = new JPanel();
        menuPanel.add(menuTextPanel);
        menuTextPanel.setLayout(new GridLayout(1, 0, 0, 0));

        JLabel helloLabel = new JLabel("Hello "+ currentUsername);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(helloLabel);

        JLabel balanceLabel = new JLabel("Balance: " + String.valueOf(organizerController.getBalance(currentUsername))+ "$");
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(balanceLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addEventButton = new JButton("Add Event");

        addEventButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                OrganizerAddEvent organizerAddEvent = new OrganizerAddEvent(currentUsername);
            }
        });

        buttonPanel.add(addEventButton);

        JButton editEventButton = new JButton("Edit Event");

        editEventButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                OrganizerEditEvent organizerAddEvent = new OrganizerEditEvent(currentUsername);
            }
        });

        buttonPanel.add(editEventButton);



        frame.getContentPane().add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
