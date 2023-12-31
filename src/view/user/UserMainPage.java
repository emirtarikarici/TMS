package view.user;


import controller.DatabaseConnection;
import controller.EventController;
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
public class UserMainPage extends JFrame{

    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserMainPage window = new UserMainPage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public UserMainPage() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("User Main Page");
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
                UserMainPage userMainPage = new UserMainPage();
            }
        });
        menuButtonPanel.add(homeButton);

        JButton historyButton = new JButton("History");
        menuButtonPanel.add(historyButton);

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

        JLabel helloLabel = new JLabel("Hello Hüseyin Acemli");
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(helloLabel);

        JLabel balanceLabel = new JLabel("Balance");
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(balanceLabel);









        DefaultTableModel model = new DefaultTableModel( ) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };



        //Controller part
        EventController eventController = new EventController(new DatabaseConnection().getConnection()) ;
        ArrayList<Event> upcomingEventList =   eventController.getUpcomingEvents();
        String col[] = {"ID","Name","Organizer","Date","Location","Capacity","Sold"};
        for (String colName: col){
            model.addColumn(colName);
        }
        for (Event event : upcomingEventList){
            Object [] rowData = {event.getId(),event.getName(),event.getOrganizerUsername(),event.getDate(),event.getLocation(),event.getCapacity(),event.getSold()};
            model.addRow(rowData);
        }





        JPanel tablePanel = new JPanel();
        frame.getContentPane().add(tablePanel, BorderLayout.CENTER);

        JTable table = new JTable(model);
        table.setFocusable(false);

        //row selection active
        table.setRowSelectionAllowed(true);

        //set choosing only one row
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        //set column reordering to false
        table.getTableHeader().setReorderingAllowed(false);

        // Set column resizing to false
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);



        //create scroll item
        JScrollPane pane = new JScrollPane(table);

        tablePanel.add(pane);



        JPanel panelButton = new JPanel();

        tablePanel.add(panelButton);
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.Y_AXIS));

        JButton bookingButton = new JButton("Book Ticket");
        panelButton.add(bookingButton);
        bookingButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });


        panelButton.add(Box.createVerticalStrut(10));
        JButton refreshButton = new JButton("Refresh Table");
        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setRowCount(0);
                ArrayList<Event> upcomingEventList =   eventController.getUpcomingEvents();
                for (Event event : upcomingEventList){
                    Object [] rowData = {event.getId(),event.getName(),event.getOrganizerUsername(),event.getDate(),event.getLocation(),event.getCapacity(),event.getSold()};
                    model.addRow(rowData);
                }
            }
        });
        panelButton.add(refreshButton);

        frame.setVisible(true);


    }

}
