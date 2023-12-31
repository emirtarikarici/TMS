package view.user;


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
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

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

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(1, 0, 0, 0));

        JPanel panel_2 = new JPanel();
        panel.add(panel_2);
        panel_2.setLayout(new GridLayout(0, 4, 0, 0));

        JButton btnNewButton = new JButton("Home");
        panel_2.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("History");
        panel_2.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Profile");
        panel_2.add(btnNewButton_2);

        JButton btnNewButton_3 = new JButton("Logout");
        panel_2.add(btnNewButton_3);

        JPanel panel_1 = new JPanel();
        panel.add(panel_1);
        panel_1.setLayout(new GridLayout(1,0, 0, 0));

        JLabel lblNewLabel_1 = new JLabel("Hello Hüseyin Acemli");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        panel_1.add(lblNewLabel_1);

        JLabel lblNewLabel = new JLabel("Balance");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel_1.add(lblNewLabel);







        String data[][] = {{"001","vinod","Bihar","India","Biology","65","First"},
                {"002","Raju","ABC","Kanada","Geography","58","second"},
                {"003","Aman","Delhi","India","computer","98","Dictontion"},
                {"004","Ranjan","Bangloor","India","chemestry","90","Dictontion"},
                {"001","vinod","Bihar","India","Biology","65","First"},
                {"002","Raju","ABC","Kanada","Geography","58","second"},
                {"003","Aman","Delhi","India","computer","98","Dictontion"},
                {"004","Ranjan","Bangloor","India","chemestry","90","Dictontion"},
                {"001","vinod","Bihar","India","Biology","65","First"},
                {"002","Raju","ABC","Kanada","Geography","58","second"},
                {"003","Aman","Delhi","India","computer","98","Dictontion"},
                {"004","Ranjan","Bangloor","India","chemestry","90","Dictontion"},
                {"001","vinod","Bihar","India","Biology","65","First"},
                {"002","Raju","ABC","Kanada","Geography","58","second"},
                {"003","Aman","Delhi","India","computer","98","Dictontion"},
                {"004","Ranjan","Bangloor","India","chemestry","90","Dictontion"},
                {"001","vinod","Bihar","India","Biology","65","First"},
                {"002","Raju","ABC","Kanada","Geography","58","second"},
                {"003","Aman","Delhi","India","computer","98","Dictontion"},
                {"004","Ranjan","Bangloor","India","chemestry","90","Dictontion"},
                {"001","vinod","Bihar","India","Biology","65","First"},
                {"002","Raju","ABC","Kanada","Geography","58","second"},
                {"003","Aman","Delhi","India","computer","98","Dictontion"},
                {"004","Ranjan","Bangloor","India","chemestry","90","Dictontion"},
                {"001","vinod","Bihar","India","Biology","65","First"},
                {"002","Raju","ABC","Kanada","Geography","58","second"},
                {"003","Aman","Delhi","India","computer","98","Dictontion"},
                {"004","Ranjan","Bangloor","India","chemestry","90","Dictontion"},
                {"001","vinod","Bihar","India","Biology","65","First"},
                {"002","Raju","ABC","Kanada","Geography","58","second"},
                {"003","Aman","Delhi","India","computer","98","Dictontion"},
                {"004","Ranjan","Bangloor","India","chemestry","90","Dictontion"}};
        String col[] = {"Roll","Name","State","country","Math","Marks","Grade"};
        DefaultTableModel model = new DefaultTableModel(data, col) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };





        JPanel panelTable = new JPanel();
        frame.getContentPane().add(panelTable, BorderLayout.CENTER);

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

        panelTable.add(pane);



        JPanel panelButton = new JPanel();

        panelTable.add(panelButton);
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.Y_AXIS));

        JButton btnNewButton_4 = new JButton("Book Ticket");
        panelButton.add(btnNewButton_4);
        panelButton.add(Box.createVerticalStrut(10));
        JButton btnNewButton_5 = new JButton("Refresh Table");
        panelButton.add(btnNewButton_5);

        frame.setVisible(true);


    }

}
