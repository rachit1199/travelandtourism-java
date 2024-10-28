import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Hotel class
class Hotel {
    private String name;
    private String location;
    private int price;

    public Hotel(String name, String location, int price) {
        this.name = name;
        this.location = location;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " | Location: " + location + " | Price: Rs " + price;
    }
}

// Flight class
class Flight {
    private String name;
    private String departure;
    private String destination;
    private int price;

    public Flight(String name, String departure, String destination, int price) {
        this.name = name;
        this.departure = departure;
        this.destination = destination;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " | From: " + departure + " | To: " + destination + " | Price: Rs " + price;
    }
}

// TravelBookingSystem class
class TravelBookingSystem {
    private Map<String, String> users = new HashMap<>();
    private List<Hotel> hotels = new ArrayList<>();
    private List<Flight> flights = new ArrayList<>();
    private Map<String, List<String>> bookings = new HashMap<>();

    // Register user
    public boolean registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, password);
            return true;
        }
        return false;
    }

    // User login
    public boolean loginUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    // Add hotel
    public void addHotel(Hotel hotel) {
        hotels.add(hotel);
    }

    // Add flight
    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    // Search hotels
    public String searchHotel(String location, int minPrice, int maxPrice) {
        StringBuilder result = new StringBuilder();
        for (Hotel hotel : hotels) {
            if (hotel.getLocation().equalsIgnoreCase(location) &&
                    hotel.getPrice() >= minPrice && hotel.getPrice() <= maxPrice) {
                result.append(hotel.toString()).append("\n");
            }
        }
        return result.length() > 0 ? result.toString() : "No hotels found.";
    }

    // Search flights
    public String searchFlight(String departure, String destination) {
        StringBuilder result = new StringBuilder();
        for (Flight flight : flights) {
            if (flight.getDeparture().equalsIgnoreCase(departure) &&
                    flight.getDestination().equalsIgnoreCase(destination)) {
                result.append(flight.toString()).append("\n");
            }
        }
        return result.length() > 0 ? result.toString() : "No flights found.";
    }

    // Add a booking
    public void addBooking(String username, String booking) {
        bookings.computeIfAbsent(username, k -> new ArrayList<>()).add(booking);
    }

    // Get all bookings for a user
    public List<String> getUserBookings(String username) {
        return bookings.getOrDefault(username, new ArrayList<>());
    }

    // Cancel a booking
    public void cancelBooking(String username, String booking) {
        bookings.getOrDefault(username, new ArrayList<>()).remove(booking);
    }
}

// Main class with GUI
public class TravelAndTourismWebsite extends JFrame implements ActionListener {
    private TravelBookingSystem system = new TravelBookingSystem();
    private JTextField searchField, departureField, destinationField;
    private JTextArea resultArea;
    private JTabbedPane tabbedPane;
    private String loggedInUser = null;

    public TravelAndTourismWebsite() {
        setTitle("Travel and Tourism Website");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        
        // Add some sample data
        system.addHotel(new Hotel("Hotel Blue Sky", "Chennai", 1500));
        system.addFlight(new Flight("Delta", "Chennai", "Delhi", 7000));

        tabbedPane.add("Search", createSearchPanel());
        tabbedPane.add("Bookings", createBookingsPanel());
        tabbedPane.add("Profile", createProfilePanel());
        
        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2));

        searchField = new JTextField();
        departureField = new JTextField();
        destinationField = new JTextField();
        
        JButton searchHotelButton = new JButton("Search Hotels");
        JButton searchFlightButton = new JButton("Search Flights");
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        searchHotelButton.addActionListener(this);
        searchFlightButton.addActionListener(this);
        
        panel.add(new JLabel("Search Hotels by Location:"));
        panel.add(searchField);
        panel.add(searchHotelButton);
        panel.add(new JLabel("Departure:"));
        panel.add(departureField);
        panel.add(new JLabel("Destination:"));
        panel.add(destinationField);
        panel.add(searchFlightButton);
        panel.add(new JScrollPane(resultArea));
        
        return panel;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea bookingsArea = new JTextArea(10, 30);
        bookingsArea.setEditable(false);
        JButton viewBookingsButton = new JButton("View My Bookings");
        JButton cancelBookingButton = new JButton("Cancel Selected Booking");

        viewBookingsButton.addActionListener(e -> viewBookings(bookingsArea));
        cancelBookingButton.addActionListener(e -> cancelSelectedBooking(bookingsArea));

        panel.add(viewBookingsButton, BorderLayout.NORTH);
        panel.add(new JScrollPane(bookingsArea), BorderLayout.CENTER);
        panel.add(cancelBookingButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton signUpButton = new JButton("Sign Up");
        JButton signInButton = new JButton("Sign In");
        JButton logOutButton = new JButton("Log Out");

        signUpButton.addActionListener(this);
        signInButton.addActionListener(this);
        logOutButton.addActionListener(this);

        panel.add(signUpButton);
        panel.add(signInButton);
        panel.add(logOutButton);

        return panel;
    }

    private void viewBookings(JTextArea bookingsArea) {
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "Please sign in first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> bookings = system.getUserBookings(loggedInUser);
        bookingsArea.setText("Your Bookings:\n");
        for (String booking : bookings) {
            bookingsArea.append(booking + "\n");
        }
    }

    private void cancelSelectedBooking(JTextArea bookingsArea) {
        String selectedBooking = bookingsArea.getSelectedText();
        if (selectedBooking != null && loggedInUser != null) {
            system.cancelBooking(loggedInUser, selectedBooking.trim());
            JOptionPane.showMessageDialog(this, "Booking canceled.", "Success", JOptionPane.INFORMATION_MESSAGE);
            viewBookings(bookingsArea);
        } else {
            JOptionPane.showMessageDialog(this, "No booking selected or user not signed in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Search Hotels")) {
            if (checkLogin()) searchHotels();
        } else if (command.equals("Search Flights")) {
            if (checkLogin()) searchFlights();
        } else if (command.equals("Sign Up")) {
            signUp();
        } else if (command.equals("Sign In")) {
            signIn();
        } else if (command.equals("Log Out")) {
            logOut();
        }
    }

    private boolean checkLogin() {
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "Please sign in first.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void signUp() {
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JPanel panel = new JPanel();
        
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sign Up", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (system.registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Sign-up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void signIn() {
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JPanel panel = new JPanel();

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sign In", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (system.loginUser(username, password)) {
                loggedInUser = username;
                JOptionPane.showMessageDialog(this, "Sign-in successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void logOut() {
        loggedInUser = null;
        JOptionPane.showMessageDialog(this, "Logged out successfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchHotels() {
        String location = searchField.getText();
        String minPrice = JOptionPane.showInputDialog("Enter minimum price:");
        String maxPrice = JOptionPane.showInputDialog("Enter maximum price:");

        if (location != null && minPrice != null && maxPrice != null) {
            String results = system.searchHotel(location, Integer.parseInt(minPrice), Integer.parseInt(maxPrice));
            resultArea.setText("Hotel Search Results:\n" + results);
        }
    }

    private void searchFlights() {
        String departure = departureField.getText();
        String destination = destinationField.getText();

        if (departure != null && destination != null) {
            String results = system.searchFlight(departure, destination);
            resultArea.setText("Flight Search Results:\n" + results);
        }
    }

    public static void main(String[] args) {
        new TravelAndTourismWebsite();
    }
}
