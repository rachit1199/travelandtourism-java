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
    private List<String> bookings = new ArrayList<>();

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

    // Add a booking to the list
    public void addBooking(String booking) {
        bookings.add(booking);
    }

    // View all bookings
    public List<String> getBookings() {
        return new ArrayList<>(bookings); // Return a copy to avoid modification
    }
}

// Main class with GUI
public class TravelAndTourismWebsite extends JFrame implements ActionListener {
    private TravelBookingSystem system = new TravelBookingSystem();
    private JTextField searchField, departureField, destinationField;
    private JTextArea resultArea;
    private String loggedInUser = null; // Track the logged-in user

    public TravelAndTourismWebsite() {
        // Setting up the JFrame
        setTitle("Travel and Tourism Website");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Add some sample data
        system.addHotel(new Hotel("Hotel Blue Sky", "chennai", 1500));
        system.addHotel(new Hotel("Ocean View", "delhi", 2000));
        system.addHotel(new Hotel("The Taj Mahal Palace", "maharashtra", 1800));
        system.addHotel(new Hotel("Rambagh palace", "rajasthan", 1500));
        system.addHotel(new Hotel("Novotel Ahmedabad", "gujarat", 2000));
        system.addHotel(new Hotel("Swati", "goa", 1800));
        system.addHotel(new Hotel("Mountain Inn", "andhra pradesh", 1800));

        system.addFlight(new Flight("Delta", "chennai", "delhi", 7000));
        system.addFlight(new Flight("Indian Airlines", "maharashtra", "rajasthan", 5500));
        system.addFlight(new Flight("Indigo", "goa", "gujarat", 5000));
        system.addFlight(new Flight("SpiceJet", "goa", "andhra pradesh", 7000));
        system.addFlight(new Flight("Delta", "chennai", "maharashtra", 7000));

        // Create and add components
        JButton signUpButton = new JButton("Sign Up");
        JButton signInButton = new JButton("Sign In");
        JButton logOutButton = new JButton("Log Out");

        JLabel searchLabel = new JLabel("Search Hotels by Location:");
        searchField = new JTextField(15);
        JButton searchHotelButton = new JButton("Search Hotels");

        JLabel flightLabel = new JLabel("Search Flights (Departure - Destination):");
        departureField = new JTextField(7);
        destinationField = new JTextField(7);
        JButton searchFlightButton = new JButton("Search Flights");

        resultArea = new JTextArea(8, 40);
        resultArea.setEditable(false);

        // Buttons for booking actions
        JButton viewBookingsButton = new JButton("View Bookings");
        JButton bookHotelButton = new JButton("Book Hotel");
        JButton bookFlightButton = new JButton("Book Flight");

        // Add action listeners
        signUpButton.addActionListener(this);
        signInButton.addActionListener(this);
        logOutButton.addActionListener(this);
        searchHotelButton.addActionListener(this);
        searchFlightButton.addActionListener(this);
        viewBookingsButton.addActionListener(this);
        bookHotelButton.addActionListener(this);
        bookFlightButton.addActionListener(this);

        // Add components to the JFrame
        add(signUpButton);
        add(signInButton);
        add(logOutButton);
        add(searchLabel);
        add(searchField);
        add(searchHotelButton);
        add(flightLabel);
        add(departureField);
        add(destinationField);
        add(searchFlightButton);
        add(resultArea);

        // Add buttons for booking
        add(bookHotelButton);
        add(bookFlightButton);
        add(viewBookingsButton);

        setVisible(true);
    }

    // Action handler
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Sign Up")) {
            signUp();
        } else if (command.equals("Sign In")) {
            signIn();
        } else if (command.equals("Log Out")) {
            logOut();
        } else if (command.equals("View Bookings")) {
            viewBookings();
        } else if (command.equals("Search Hotels")) {
            if (checkLogin()) {
                searchHotels();
            }
        } else if (command.equals("Search Flights")) {
            if (checkLogin()) {
                searchFlights();
            }
        } else if (command.equals("Book Hotel")) {
            if (checkLogin()) {
                bookHotel();
            }
        } else if (command.equals("Book Flight")) {
            if (checkLogin()) {
                bookFlight();
            }
        }
    }

    // Sign-up window
    private void signUp() {
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createHorizontalStrut(15)); // Spacing
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Sign Up", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (system.registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Sign-up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Username already taken.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Sign-in window
    private void signIn() {
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createHorizontalStrut(15)); // Spacing
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Sign In", JOptionPane.OK_CANCEL_OPTION);

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

    // Log-out
    private void logOut() {
        if (loggedInUser != null) {
            loggedInUser = null;
            JOptionPane.showMessageDialog(this, "Logged out successfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No user is currently logged in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Ensure the user is logged in
    private boolean checkLogin() {
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "Please sign in first.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // View bookings
    private void viewBookings() {
        List<String> bookings = system.getBookings();
        StringBuilder message = new StringBuilder("Bookings:\n");
        for (String booking : bookings) {
            message.append(booking).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString(), "Bookings", JOptionPane.INFORMATION_MESSAGE);
    }

    // Search hotels
    private void searchHotels() {
        String location = searchField.getText();
        JTextField minPriceField = new JTextField(5);
        JTextField maxPriceField = new JTextField(5);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Min Price:"));
        panel.add(minPriceField);
        panel.add(new JLabel("Max Price:"));
        panel.add(maxPriceField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter Price Range", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int minPrice = Integer.parseInt(minPriceField.getText());
                int maxPrice = Integer.parseInt(maxPriceField.getText());

                String searchResult = system.searchHotel(location, minPrice, maxPrice);
                resultArea.setText(searchResult);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for the price range.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Search flights
    private void searchFlights() {
        String departure = departureField.getText();
        String destination = destinationField.getText();

        String searchResult = system.searchFlight(departure, destination);
        resultArea.setText(searchResult);
    }

    // Book hotel
    private void bookHotel() {
        String selectedHotel = resultArea.getSelectedText();
        if (selectedHotel != null) {
            system.addBooking("Hotel: " + selectedHotel + " - Booked by: " + loggedInUser);
            JOptionPane.showMessageDialog(this, "Hotel booked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No hotel selected for booking.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Book flight
    private void bookFlight() {
        String selectedFlight = resultArea.getSelectedText();
        if (selectedFlight != null) {
            system.addBooking("Flight: " + selectedFlight + " - Booked by: " + loggedInUser);
            JOptionPane.showMessageDialog(this, "Flight booked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No flight selected for booking.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new TravelAndTourismWebsite();
    }
}
