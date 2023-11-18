import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class UCSIBookingSystemGUI {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardsPanel;
    private JTextField userText;
    private JPasswordField passwordText;
    private DefaultTableModel membersTableModel;

    public UCSIBookingSystemGUI() {
        // Initialize the main frame and card layout
        frame = new JFrame("UCSI Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // Add different panels for login, admin, and member
        cardsPanel.add(createLoginPanel(), "Login");
        cardsPanel.add(createAdminPanel(), "Admin");
        cardsPanel.add(createMemberPanel(), "Member");

        frame.add(cardsPanel);
        frame.setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("User ID:");
        userText = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> authenticateUser());

        loginPanel.add(userLabel);
        loginPanel.add(userText);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordText);
        loginPanel.add(new JLabel()); // Placeholder for layout
        loginPanel.add(loginButton);

        return loginPanel;
    }

    private void authenticateUser() {
        String userId = userText.getText();
        String password = new String(passwordText.getPassword());

        // Run the authentication in a separate thread to avoid freezing the UI
        new Thread(() -> {
            try {
                if (validateCredentials(userId, password)) {
                    String role = getUserRole(userId);
                    SwingUtilities.invokeLater(() -> {
                        if ("Admin".equals(role)) {
                            cardLayout.show(cardsPanel, "Admin");
                        } else if ("Member".equals(role)) {
                            cardLayout.show(cardsPanel, "Member");
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(frame, "Invalid login credentials", "Login Error", JOptionPane.ERROR_MESSAGE));
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(frame, "Error during authentication: " + e.getMessage(), "Authentication Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }


    private boolean validateCredentials(String userId, String inputPassword) {
        String url = "jdbc:mysql://localhost:3306/ucsibookingsystem";
        String dbUser = "root";
        String dbPassword = "toshiro1944-";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, dbUser, dbPassword);
            String query = "SELECT password_hash FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");
                return checkPassword(inputPassword, storedPasswordHash);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace(); // Replace with proper logging
            }
        }
        return false;
    }

    private boolean checkPassword(String password, String storedPasswordHash) {
        return BCrypt.checkpw(password, storedPasswordHash);
    }


    private String getUserRole(String userId) {
        String url = "jdbc:mysql://localhost:3306/ucsibookingsystem";
        String dbUser = "root";
        String dbPassword = "toshiro1944-";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, dbUser, dbPassword);
            String query = "SELECT role FROM users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace(); // Replace with proper logging
            }
        }
        return null;
    }





    private JPanel createAdminPanel() {
        JPanel adminPanel = new JPanel(new BorderLayout());

        // Top panel with buttons
        JButton manageMembersButton = new JButton("Manage Members");
        manageMembersButton.addActionListener(e -> manageMembers());

        JButton viewBookingsButton = new JButton("View Bookings");
        viewBookingsButton.addActionListener(e -> viewBookings());

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(manageMembersButton);
        topPanel.add(viewBookingsButton);

        // Table for displaying member information
        membersTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Membership Type"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // This makes the table cells non-editable
                return false;
            }
        };

        JTable membersTable = new JTable(membersTableModel);
        membersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        membersTable.setAutoCreateRowSorter(true); // Enable sorting

        // Add a mouse listener for double-click events on table rows
        membersTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    // Open a member management dialog with selected member's data
                    // Example: manageSelectedMember(membersTableModel.getValueAt(row, 0).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(membersTable);
        adminPanel.add(scrollPane, BorderLayout.CENTER);

        // Refresh the table with the latest member data
        refreshMembersTable();

        return adminPanel;
    }
    private void refreshMembersTable() {
        // Clear existing data
        membersTableModel.setRowCount(0);

        // Fetch member data from the database and populate the table
        List<Member> members = fetchMembers();
        for (Member member : members) {
            membersTableModel.addRow(new Object[]{member.getId(), member.getName(), member.getMembershipType()});
        }
    }


    private void manageMembers() {
        JDialog memberDialog = new JDialog(frame, "Manage Members", true);
        memberDialog.setLayout(new BorderLayout());
        memberDialog.setSize(400, 300);

        // Components for adding or updating member information
        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("ID:"));
        JTextField idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Membership Type:"));
        JTextField typeField = new JTextField();
        inputPanel.add(typeField);

        JButton addButton = new JButton("Add Member");
        addButton.addActionListener(e -> addMember(idField.getText(), nameField.getText(), typeField.getText()));

        JButton updateButton = new JButton("Update Member");
        updateButton.addActionListener(e -> updateMember(idField.getText(), nameField.getText(), typeField.getText()));

        JButton deleteButton = new JButton("Delete Member");
        deleteButton.addActionListener(e -> deleteMember(idField.getText()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        memberDialog.add(inputPanel, BorderLayout.CENTER);
        memberDialog.add(buttonPanel, BorderLayout.SOUTH);

        memberDialog.setVisible(true);
    }
    private void addMember(String id, String name, String type) {
        String sql = "INSERT INTO members (member_id, name, membershipType) VALUES (?, ?, ?)";
        executeUpdate(sql, id, name, type);
        refreshMembersTable();
    }

    private void updateMember(String id, String name, String type) {
        String sql = "UPDATE members SET name = ?, membershipType = ? WHERE member_id = ?";
        executeUpdate(sql, name, type, id); // Note the order of parameters
        refreshMembersTable();
    }

    private void deleteMember(String id) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        executeUpdate(sql, id);
        refreshMembersTable();
    }

    private void executeUpdate(String sql, Object... params) {
        String url = "jdbc:mysql://localhost:3306/ucsibookingsystem";
        String user = "root";
        String password = "toshiro1944-";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging in production
        }
    }


    private void viewBookings() {
        List<Booking> bookings = fetchBookings();

        // Clear the existing table data
        membersTableModel.setRowCount(0);
        membersTableModel.setColumnIdentifiers(new String[]{"Booking ID", "Facility", "Date"}); // Adjust column names for bookings

        // Populate the table with booking data
        for (Booking booking : bookings) {
            membersTableModel.addRow(new Object[]{booking.getId(), booking.getFacility(), booking.getDate()});
        }
    }
    private List<Booking> fetchBookings() {
        List<Booking> bookings = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/ucsibookingsystem";
        String user = "root";
        String password = "toshiro1944-";
        String sql = "SELECT booking_id, facility_name, booking_date FROM bookings";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("booking_id");
                String facility = rs.getString("facility_name");
                Date date = rs.getDate("booking_date");
                bookings.add(new Booking(id, facility, date)); // Assuming Booking is a class with these fields
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging in production
        }

        return bookings;
    }


    private JPanel createMemberPanel() {
        JPanel memberPanel = new JPanel(new FlowLayout());

        JButton bookFacilityButton = new JButton("Book Facility");
        bookFacilityButton.addActionListener(e -> bookFacility());

        memberPanel.add(bookFacilityButton);

        return memberPanel;
    }

    private void bookFacility() {
        JDialog bookingDialog = new JDialog(frame, "Book Facility", true);
        bookingDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        bookingDialog.setSize(400, 300);

        // Components for facility booking
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        bookingDialog.add(new JLabel("Facility:"), gbc);

        JComboBox<String> facilityComboBox = new JComboBox<>(new String[]{"Gym", "Pool", "Tennis Court"}); // Add facilities here
        gbc.gridx = 1;
        gbc.gridy = 0;
        bookingDialog.add(facilityComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        bookingDialog.add(new JLabel("Date:"), gbc);

        JDatePickerImpl datePicker = createDatePicker();
        gbc.gridx = 1;
        gbc.gridy = 1;
        bookingDialog.add(datePicker, gbc);

        JButton bookButton = new JButton("Book");
        bookButton.addActionListener(e -> {
            String facility = (String) facilityComboBox.getSelectedItem();
            Date selectedDate = (Date) datePicker.getModel().getValue();
            private void handleBooking(String facility, Date date) {
                if (facility == null || date == null) {
                    JOptionPane.showMessageDialog(frame, "Please select a facility and date.", "Booking Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String memberId = getCurrentMemberId(); // Replace with actual logic to get current member's ID
                if (isFacilityAvailable(facility, date)) {
                    if (addBookingToDatabase(memberId, facility, date)) {
                        JOptionPane.showMessageDialog(frame, "Booking successful!", "Booking", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to book facility.", "Booking Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Facility is not available on the selected date.", "Booking Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private boolean isFacilityAvailable(String facility, Date date) {
                // Check database to see if the facility is available on the given date
                // This is a placeholder logic
                return true; // Assume it's available for now
            }

            private boolean addBookingToDatabase(String memberId, String facility, Date date) {
                String sql = "INSERT INTO bookings (member_id, facility_name, booking_date) VALUES (?, ?, ?)";
                String url = "jdbc:mysql://localhost:3306/ucsibookingsystem";
                String user = "root";
                String password = "toshiro1944-";

                try (Connection conn = DriverManager.getConnection(url, user, password);
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, memberId);
                    stmt.setString(2, facility);
                    stmt.setDate(3, new java.sql.Date(date.getTime()));
                    int affectedRows = stmt.executeUpdate();
                    return affectedRows > 0;
                } catch (SQLException e) {
                    e.printStackTrace(); // Replace with proper logging
                    return false;
                }
            }
            handleBooking(facility, selectedDate);
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        bookingDialog.add(bookButton, gbc);

        bookingDialog.setVisible(true);
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }


    private void handleBooking(String facility, Date date) {
        if (facility == null || date == null) {
            JOptionPane.showMessageDialog(frame, "Please select a facility and date.", "Booking Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the date is not in the past
        if (date.before(new Date())) {
            JOptionPane.showMessageDialog(frame, "Cannot book on a past date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Checking facility availability
        if (!isFacilityAvailable(facility, date)) {
            JOptionPane.showMessageDialog(frame, "Facility is not available on the selected date.", "Unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Assuming you have a method to get the current user's ID
        String currentUserId = getCurrentUserId();

        // Add the booking to the database
        if (addBooking(currentUserId, facility, date)) {
            JOptionPane.showMessageDialog(frame, "Facility booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Unable to book the facility. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isFacilityAvailable(String facility, Date date) {
        // Logic to check if the facility is available on the specified date
        // Placeholder for database query
        return true; // Assuming it's available
    }

    private boolean addBooking(String userId, String facility, Date date) {
        // Logic to add the booking to the database
        // Placeholder for database interaction
        return true; // Assuming booking is successful
    }

    private String getCurrentUserId() {
        // Logic to obtain the current user's ID
        // Placeholder for actual implementation
        return "user123"; // Example user ID
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UCSIBookingSystemGUI());
    }
}
