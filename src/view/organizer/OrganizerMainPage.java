package view.organizer;


import controller.*;
import model.Event;
import view.LoginPage;
import view.ProfilePage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class OrganizerMainPage extends JFrame{

    private JFrame frame;
    private String currentUsername;

    private EventController eventController;
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
        eventController = new EventController(new DatabaseConnection().getConnection());
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
        menuTextPanel.setLayout(new GridLayout(1,0, 0, 0));

        JLabel helloLabel = new JLabel(currentUsername);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(helloLabel);

        JLabel balanceLabel = new JLabel(String.valueOf(organizerController.getBalance(currentUsername)));
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




        String col[] = {"ID","Name","Organizer","Price","Date","Location","Capacity","Sold","Status"};
        for (String colName: col){
            model.addColumn(colName);
        }
        ArrayList<Event> ticketArrayList =   eventController.getAllEvents();
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
            if("Active".equals(status)) {
                Object[] rowData = {event.getId(), event.getName(), event.getOrganizerUsername(), event.getPrice(), event.getDate(), event.getLocation(), event.getCapacity(), event.getSold(), status};
                model.addRow(rowData);
            }
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

        JScrollPane pane = new JScrollPane(table);

        panel.add(pane);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        DefaultRowSorter<?, ?> rowSorter = (DefaultRowSorter<?, ?>) table.getRowSorter();
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
        rowSorter.setSortKeys(sortKeys);
        rowSorter.sort();

        frame.add(panel);

        frame.setVisible(true);


    }

}
