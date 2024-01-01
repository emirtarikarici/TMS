package view.organizer;


import controller.*;
import view.LoginPage;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import model.*;
public class OrganizerMainPage extends JFrame{

    private JFrame frame;
    public String currentUsername;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OrganizerMainPage window = new OrganizerMainPage("Temp");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public OrganizerMainPage(String currentUsername) {
        this.currentUsername = currentUsername;
        initialize();
    }

    private void initialize() {
        TicketController ticketController = new TicketController(new DatabaseConnection().getConnection());
        TransactionController transactionController = new TransactionController(new DatabaseConnection().getConnection());
        OrganizerController organizerController = new OrganizerController(new DatabaseConnection().getConnection());
        frame = new JFrame("Organizer Main Page");
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

        JLabel helloLabel = new JLabel(currentUsername);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(helloLabel);

        JLabel balanceLabel = new JLabel(String.valueOf(organizerController.getBalance(currentUsername)));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(balanceLabel);


        frame.setVisible(true);


    }

}
