package view.user;


import controller.*;
import view.LoginPage;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.FlowLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Color;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import model.*;
import view.ProfilePage;

public class UserMainPage extends JFrame{

    private JFrame frame;
    public String currentUsername;
    private EventController eventController;
    private TicketController ticketController;
    private TransactionController transactionController;
    private UserController userController;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserMainPage window = new UserMainPage("Temp");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public UserMainPage(String currentUsername) {
        this.currentUsername = currentUsername;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        ticketController = new TicketController(new DatabaseConnection().getConnection());
        transactionController = new TransactionController(new DatabaseConnection().getConnection());
        userController = new UserController(new DatabaseConnection().getConnection());
        eventController = new EventController(new DatabaseConnection().getConnection()) ;
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
                UserMainPage userMainPage = new UserMainPage(currentUsername);
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













        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50,50,200,200);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.add("Upcoming Events",getEventsPanel(currentUsername));
        tabbedPane.add("My Tickets",getYourTicketPanel(currentUsername));
        tabbedPane.add("Expired Events",displayExpiredEventPanel(currentUsername));


        frame.setVisible(true);


    }

    public JPanel getYourTicketPanel(String currentUsername){
        JPanel panel = new JPanel();


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


        String col[] = {"TicketID","Event Name","Price","Date","Location","Status"};
        for (String colName: col){
            model.addColumn(colName);
        }
        ArrayList<Ticket> ticketArrayList =   ticketController.getTicketsByUsername(currentUsername);
        for (Ticket ticket : ticketArrayList){
            Event event = eventController.getEventById(ticket.getEventId());

            String status = "null";
            if (ticket.getStatus() == 0){
                status = "Active";
            }
            else if (ticket.getStatus() == 1){
                status = "Cancelled";
            }
            Object [] rowData = {ticket.getTicketNumber(),event.getName(),event.getPrice(),event.getDate(),event.getLocation(),status};
            model.addRow(rowData);
        }

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

        panel.add(pane);



        JPanel panelButton = new JPanel();

        panel.add(panelButton);
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.Y_AXIS));

        JButton cancelButton = new JButton("Cancel Ticket");
        panelButton.add(cancelButton);
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1){
                    JOptionPane.showMessageDialog(new JFrame(), "There is no selected event");
                }
                else{
                    int ticketId = (int)(table.getValueAt(selectedRow, 0));
                    int transactionNumber = transactionController.getTransactionByTicket(ticketId).getTransactionNumber();

                    if(transactionController.cancelTransaction(transactionNumber)){
                        JOptionPane.showMessageDialog(new JFrame(), "Ticket is cancelled successfully.");
                        frame.dispose();
                        UserMainPage userMainPage = new UserMainPage(currentUsername);
                    }
                    else{
                        JOptionPane.showMessageDialog(new JFrame(), "Ticket cannot be cancelled");

                    }
                }



            }
        });


        panelButton.add(Box.createVerticalStrut(10));
        JButton refreshButton = new JButton("Refresh Table");
        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setRowCount(0);
                ArrayList<Ticket> ticketArrayList =   ticketController.getTicketsByUsername(currentUsername);
                for (Ticket ticket : ticketArrayList){
                    String status = "null";
                    if (ticket.getStatus() == 0){
                        status = "Active";
                    }
                    else if (ticket.getStatus() == 1){
                        status = "Cancelled";
                    }
                    Object [] rowData = {ticket.getTicketNumber(),ticket.getEventId(),status};
                    model.addRow(rowData);
                }

            }
        });
        panelButton.add(refreshButton);

        return panel;
    }

    public JPanel getEventsPanel(String currentUsername){

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

        ArrayList<Event> upcomingEventList =   eventController.getUpcomingEvents();
        String col[] = {"ID","Name","Organizer","Price","Date","Location","Capacity","Sold"};
        for (String colName: col){
            model.addColumn(colName);
        }
        for (Event event : upcomingEventList){
            Object [] rowData = {event.getId(),event.getName(),event.getOrganizerUsername(),event.getPrice(),event.getDate(),event.getLocation(),event.getCapacity(),event.getSold()};
            model.addRow(rowData);
        }

        JPanel tablePanel = new JPanel();


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
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1){
                    JOptionPane.showMessageDialog(new JFrame(), "There is no selected event");
                }
                else{
                    float price = 100;
                    int eventId = (int)(table.getValueAt(selectedRow, 0));
                    Event event = eventController.getEventById(eventId);
                    int ticketId = ticketController.createTicket(currentUsername,eventId);
                    int transactionId = transactionController.createTransaction(currentUsername,event.getOrganizerUsername(),ticketId);



                    //refresh Page
                    if (transactionId != -1){
                        JOptionPane.showMessageDialog(new JFrame(), "Ticket is booked successfully.  ");
                        frame.dispose();
                        UserMainPage userMainPage = new UserMainPage(currentUsername);
                    }


                }



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
                    Object [] rowData = {event.getId(),event.getName(),event.getOrganizerUsername(),event.getPrice(),event.getDate(),event.getLocation(),event.getCapacity(),event.getSold()};
                    model.addRow(rowData);
                }

            }
        });
        panelButton.add(refreshButton);
        return tablePanel;
    }

    public JPanel displayExpiredEventPanel(String currentUsername){
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

        ArrayList<Event> expiredEvents =   eventController.getExpiredEvents();
        String col[] = {"ID","Name","Organizer","Price","Date","Location","Capacity","Sold"};
        for (String colName: col){
            model.addColumn(colName);
        }
        for (Event event : expiredEvents){
            Object [] rowData = {event.getId(),event.getName(),event.getOrganizerUsername(),event.getPrice(),event.getDate(),event.getLocation(),event.getCapacity(),event.getSold()};
            model.addRow(rowData);
        }

        JPanel tablePanel = new JPanel();


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




        panelButton.add(Box.createVerticalStrut(10));
        JButton refreshButton = new JButton("Refresh Table");
        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                model.setRowCount(0);
                ArrayList<Event> expiredEvents =   eventController.getExpiredEvents();
                for (Event event : expiredEvents){
                    Object [] rowData = {event.getId(),event.getName(),event.getOrganizerUsername(),event.getPrice(),event.getDate(),event.getLocation(),event.getCapacity(),event.getSold()};
                    model.addRow(rowData);
                }

            }
        });
        panelButton.add(refreshButton);
        return tablePanel;
    }


}
