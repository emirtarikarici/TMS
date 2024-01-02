package view.organizer;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import controller.*;
import model.Event;
import view.LoginPage;

import java.awt.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

import model.*;
import view.ProfilePage;
import java.text.DecimalFormat;
import javax.swing.table.TableRowSorter;
import javax.swing.text.NumberFormatter;
import java.sql.Timestamp;
public class OrganizerMainPage extends JFrame{

    private JFrame frame;
    public String currentUsername;
    private EventController eventController;
    private TicketController ticketController;
    private TransactionController transactionController;
    private OrganizerController organizerController;
    /**
     * Launch the application.
     */
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

    /**
     * Create the application.
     */
    public OrganizerMainPage(String currentUsername) {
        this.currentUsername = currentUsername;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        ticketController = new TicketController(new DatabaseConnection().getConnection());
        transactionController = new TransactionController(new DatabaseConnection().getConnection());
        organizerController = new OrganizerController(new DatabaseConnection().getConnection());
        eventController = new EventController(new DatabaseConnection().getConnection()) ;
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


        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new GridLayout(0, 3, 0, 0));
        tempPanel.add(new JLabel());
        tempPanel.add(new JLabel());


        menuTextPanel.add(tempPanel);




        JLabel balanceLabel = new JLabel("Balance: "+ organizerController.getBalance(currentUsername)+ "$" );
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuTextPanel.add(balanceLabel);













        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50,50,200,200);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.add("Upcoming Events",getEventsPanel(currentUsername));
        tabbedPane.add("My Events",getYourEventPanel(currentUsername));
        tabbedPane.add("Expired Events",displayExpiredEventPanel(currentUsername));


        frame.setVisible(true);


    }

    public JPanel getYourEventPanel(String currentUsername){
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


        String col[] = {"ID","Name","Organizer","Price","Date","Location","Capacity","Sold"};
        for (String colName: col){
            model.addColumn(colName);
        }


        ColorRenderer renderer = refreshMyEventTable(model);
        JTable table = new JTable(model);
        table.setDefaultRenderer(Object.class, renderer);
        table.setFocusable(false);


        table.setRowSelectionAllowed(true);

        table.setPreferredScrollableViewportSize(new Dimension(800, 300));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



        table.getTableHeader().setReorderingAllowed(false);


        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);




        JScrollPane pane = new JScrollPane(table);

        panel.add(pane);



        JPanel panelButton = new JPanel();

        panel.add(panelButton);
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.Y_AXIS));

        JButton editEventButton = new JButton("Edit Event");
        panelButton.add(editEventButton);
        editEventButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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


        panelButton.add(Box.createVerticalStrut(10));
        JButton addEventButton = new JButton("Add Event");
        addEventButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                OrganizerAddEvent organizerAddEvent = new OrganizerAddEvent(currentUsername);
            }
        });
        panelButton.add(addEventButton);

        return panel;
    }

    public JPanel getEventsPanel(String currentUsername){

        DefaultTableModel model = new DefaultTableModel( ) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ArrayList<Event> upcomingEvents =  eventController.getUpcomingEvents();
        String col[] = {"ID","Name","Organizer","Price","Date","Location","Capacity","Sold"};
        for (String colName: col){
            model.addColumn(colName);
        }
        for (Event event : upcomingEvents){
            Object [] rowData = {event.getId(),event.getName(),event.getOrganizerUsername(),event.getPrice(),event.getDate(),event.getLocation(),event.getCapacity(),event.getSold()};
            model.addRow(rowData);
        }

        JPanel tablePanel = new JPanel();


        JTable table = new JTable(model);
        table.setFocusable(false);

        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setRowSelectionAllowed(true);


        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



        table.getTableHeader().setReorderingAllowed(false);



        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);




        JScrollPane pane = new JScrollPane(table);

        tablePanel.add(pane);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        DefaultRowSorter<?, ?> rowSorter = (DefaultRowSorter<?, ?>) table.getRowSorter();
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
        rowSorter.setSortKeys(sortKeys);
        rowSorter.sort();



        JPanel panelButton = new JPanel();

        tablePanel.add(panelButton);
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.Y_AXIS));


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
                return false;
            }
        };

        ArrayList<Event> expiredEvents = eventController.getExpiredEvents();
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

        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setRowSelectionAllowed(true);


        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



        table.getTableHeader().setReorderingAllowed(false);


        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.yellow);




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



    private static class ColorRenderer extends DefaultTableCellRenderer {
        private final Map<String, Color> colorMap = new HashMap<>();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column) {
            setBackground(null);
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            getColorForCell(row, column).ifPresent(this::setBackground);
            return this;
        }

        public void setColorForCell(int row, int col, Color color) {
            colorMap.put(row + ":" + col, color);
        }

        public Optional<Color> getColorForCell(int row, int col) {
            return Optional.ofNullable(colorMap.get(row + ":" + col));
        }
    }

    private ColorRenderer refreshMyEventTable(DefaultTableModel model){
        ColorRenderer renderer = new ColorRenderer();
        ArrayList<Event> ticketArrayList =   eventController.getEventsByOrganizer(currentUsername);
        int rowCursor = 0;
        for (Event ticket : ticketArrayList){
            Event event = eventController.getEventById(ticket.getId());

            Object [] rowData = {event.getId(),event.getName(),currentUsername,event.getPrice(),event.getDate(),event.getLocation(),event.getCapacity(),event.getSold()};
            if (event.getDate().before(new Timestamp(System.currentTimeMillis()))){
                renderer.setColorForCell(rowCursor, 4, Color.RED);
            }
            else {
                renderer.setColorForCell(rowCursor, 4, Color.GREEN);
            }

            rowCursor +=1;
            model.addRow(rowData);
        }
        return renderer;
    }
}
