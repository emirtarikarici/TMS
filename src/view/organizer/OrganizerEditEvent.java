package view.organizer;

import controller.*;
import model.Event;
import model.Ticket;
import view.LoginPage;
import view.ProfilePage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;

public class OrganizerEditEvent extends JFrame {

    private JFrame frame;

    private EventController eventController;
    private TicketController ticketController;
    private TransactionController transactionController;
    public String currentUsername;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OrganizerEditEvent window = new OrganizerEditEvent("Temp");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public OrganizerEditEvent(String currentUsername) {
        this.currentUsername = currentUsername;
        initialize();
    }

    private void initialize() {
        EventController eventController = new EventController(new DatabaseConnection().getConnection());
        OrganizerController organizerController = new OrganizerController(new DatabaseConnection().getConnection());
        frame = new JFrame("Organizer Edit Event Page");
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

        JLabel helloLabel = new JLabel("Hello " + currentUsername);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(helloLabel);

        JLabel balanceLabel = new JLabel("Balance: " + String.valueOf(organizerController.getBalance(currentUsername))+ "$");
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(balanceLabel);

        JPanel panel = new JPanel();
        DefaultTableModel model = new DefaultTableModel( ) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };




        String col[] = {"TicketID","Event Name","Price","Date","Location","Status"};
        for (String colName: col){
            model.addColumn(colName);
        }
        ArrayList<Event> ticketArrayList =   eventController.getEventsByOrganizer(currentUsername);
        for (Event Event : ticketArrayList){
            Event event = eventController.getEventById(Event.getId());

            String status = "null";
            Date currentDate = new Date();
            if (event.getDate().after(currentDate)){
                status = "Active";
            }
            else {
                status = "Cancelled";
            }
            Object [] rowData = {event.getId(),event.getName(),event.getPrice(),event.getDate(),event.getLocation(),status};
            model.addRow(rowData);
        }


        JTable table = new JTable(model);
        table.setFocusable(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);

        JButton editEventButton = new JButton("Edit Event");
        editEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    Object eventId = table.getValueAt(selectedRow, 0);
                    System.out.println((Integer)eventId);
                    frame.dispose();
                    EditEvent editEvent = new EditEvent(currentUsername, (Integer)eventId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select an event to edit.");
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editEventButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane pane = new JScrollPane(table);

        panel.add(pane);

        frame.add(panel);

        frame.setVisible(true);
    }
}
