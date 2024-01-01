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
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class OrganizerAddEvent extends JFrame {

    private JFrame frame;

    public String currentUsername;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OrganizerAddEvent window = new OrganizerAddEvent("Temp");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public OrganizerAddEvent(String currentUsername) {
        this.currentUsername = currentUsername;
        initialize();
    }

    private void initialize() {
        OrganizerController organizerController = new OrganizerController(new DatabaseConnection().getConnection());
        frame = new JFrame("Organizer Add Event Page");
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

        JLabel helloLabel = new JLabel(currentUsername);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(helloLabel);

        JLabel balanceLabel = new JLabel(String.valueOf(organizerController.getBalance(currentUsername)));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(balanceLabel);

        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(row);

        JPanel rowName = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nameLabel = new JLabel("Event Name: ");
        JTextField nameTextField = new JTextField(20);
        rowName.add(nameLabel);
        rowName.add(nameTextField);
        mainPanel.add(rowName);

        JPanel rowDate = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel dateLabel = new JLabel("Event Date(yyyy-MM-dd HH:mm:ss): ");
        JTextField dateTextField = new JTextField(20);
        rowDate.add(dateLabel);
        rowDate.add(dateTextField);
        mainPanel.add(rowDate);

        JPanel rowLocation = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel locationLabel = new JLabel("Event Location: ");
        JTextField locationTextField = new JTextField(20);
        rowLocation.add(locationLabel);
        rowLocation.add(locationTextField);
        mainPanel.add(rowLocation);

        JPanel rowCapacity = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel capacityLabel = new JLabel("Event Capacity: ");
        JTextField capacityTextField = new JTextField(20);
        rowCapacity.add(capacityLabel);
        rowCapacity.add(capacityTextField);
        mainPanel.add(rowCapacity);

        JPanel rowPrice = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel priceLabel = new JLabel("Event Price: ");
        JTextField priceTextField = new JTextField(20);
        rowPrice.add(priceLabel);
        rowPrice.add(priceTextField);
        mainPanel.add(rowPrice);

        JPanel rowError = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel errorLabel = new JLabel();
        rowError.add(errorLabel);
        mainPanel.add(rowError);

        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);



        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton createButton = new JButton("Create");

        createButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Connection connection = new DatabaseConnection().getConnection();
                EventController eventController = new EventController(connection);

                if(!nameTextField.getText().isEmpty() &&
                        !locationTextField.getText().isEmpty() &&
                        !capacityTextField.getText().isEmpty() &&
                        !priceTextField.getText().isEmpty() &&
                        !dateTextField.getText().isEmpty()) {
                    String dateString = dateTextField.getText();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date parsedDate = null;
                    try {
                        parsedDate = dateFormat.parse(dateString);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    Timestamp eventDate = new Timestamp(parsedDate.getTime());
                    frame.dispose();
                    eventController.createEvent(nameTextField.getText(), currentUsername, eventDate, locationTextField.getText(), Integer.parseInt(capacityTextField.getText()), Double.parseDouble(priceTextField.getText()));
                    OrganizerAddEvent organizerAddEvent = new OrganizerAddEvent(currentUsername);
                }
                else{
                    errorLabel.setText("There are some missing data or wrong timestamp format");
                }
            }
        });

        buttonPanel.add(createButton);

        JButton cancelButton = new JButton("Cancel");

        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                OrganizerEventsPage userMainPage = new OrganizerEventsPage(currentUsername);
            }
        });

        buttonPanel.add(cancelButton);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);


        frame.setVisible(true);
    }
}
