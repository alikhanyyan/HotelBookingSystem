import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Hotel implements Serializable {
    public static final String DATABASE_FILE = "HotelDatabase.bin";
    public static final String PASSWORD = "111";

    private static final Scanner scanner = new Scanner(System.in);

    private final ArrayList<Room> rooms = new ArrayList<>();
    private final ArrayList<Customer> customers = new ArrayList<>();
    private final ArrayList<Booked> bookingsInformation = new ArrayList<>();


    // Adds room
    public void addRoom() {
        RoomType roomType = chooseRoomType();

        if (roomType == null) {
            return;
        }

        Room room = new Room(roomType);
        rooms.add(room);
        System.out.println("Room added successfully.");
    }

    // Adds customer
    public void addCustomer() {
        System.out.print("\nEnter name: ");
        String name = scanner.nextLine();
        while (name.isEmpty()) {
            name = scanner.nextLine();
        }

        System.out.print("Enter contact email: ");
        String email = scanner.nextLine();
        while (email.isEmpty()) {
            email = scanner.nextLine();
        }

        if (getCustomerId(name, email) != null) {
            System.out.println("Already registered.");
            return;
        }

        Customer customer = new Customer(name, email);
        customers.add(customer);
        System.out.println("Customer added successfully.");
    }

    // Displays all registered customers with relevant information or suggest to add customers, if necessary
    public void displayOrAddCustomersIfNecessary() {
        if (customers.isEmpty()) {
            System.out.println("\nThere are no registered customers.");
            boolean exit = false;
            while (!exit) {
                System.out.println("Do want to register new customer?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> {
                        addCustomer();
                        exit = true;
                    }
                    case "2" -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        System.out.println("\nRegistered customers:");
        for (Customer customer : customers) {
            System.out.println(customer.toString());
        }
    }

    // Displays all rooms with relevant information or suggest to add customers, if necessary
    public void displayOrAddRoomsIfNecessary() {
        if (rooms.isEmpty()) {
            System.out.println("\nThere are no rooms.");
            boolean exit = false;
            while (!exit) {
                System.out.println("Do want to add new room?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> {
                        addRoom();
                        exit = true;
                    }
                    case "2" -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        System.out.println("\nRooms:");
        for (Room room : rooms) {
            System.out.println(room.toString());
        }
    }

    // Returns the index of customer by ID
    private int findCustomerByID(int id) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    // Returns index of the first room whose type fits
    private int findRoomByType(RoomType roomType) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomType() == roomType && rooms.get(i).checkIsAvailable()) {
                return i;
            }
        }
        return -1;
    }

    // For choosing room type
    private RoomType chooseRoomType() {
        RoomType roomType = null;
        boolean exit = false;
        while (!exit) {
            System.out.println("\nChoose room type:");
            System.out.println("1. Single room");
            System.out.println("2. Double room");
            System.out.println("3. Deluxe room");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    roomType = RoomType.SINGLE;
                    exit = true;
                }
                case "2" -> {
                    roomType = RoomType.DOUBLE;
                    exit = true;
                }
                case "3" -> {
                    roomType = RoomType.DELUXE;
                    exit = true;
                }
                case "0" -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        return roomType;
    }

    // Generates bill by room type
    private double generateBill(int daysCount, Room room) {
        return daysCount * room.getPricePerDay() * 1.2 * 1.1;
    }

    // Gets customer ID by name and email
    public Customer getCustomerId(String name, String email) {
        for (Customer customer : customers) {
            if (email.equals(customer.getEmail()) && name.equals(customer.getName())) {
                return customer;
            }
        }
        return null;
    }

    // Customer registration at login
    public Customer registration(String name, String email) {
        Customer customer = new Customer(name, email);
        customers.add(customer);
        System.out.println("You successfully registered.");
        
        return customer;
    }

    // Fills in the data required for booking
    private void booking(int customerIndex) {
        RoomType roomType = chooseRoomType();
        if (roomType == null) {
            return;
        }

        int roomIndex = findRoomByType(roomType);
        while (roomIndex == -1) {
            System.out.println("\nThere are no available rooms of that type. Do you want to try other type or exit?");

            System.out.println("1. Try other type");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    roomType = chooseRoomType();
                    if (roomType == null) {
                        return;
                    }

                    roomIndex = findRoomByType(roomType);
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.print("\nInput start date in 'dd-MM-yyyy' format: ");
        LocalDate startDate = inputStartDate();

        System.out.print("\nHow many days do you want to book a room for? ");
        int daysCount = 0;
        while (daysCount == 0) {
            try {
                daysCount = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("\nPlease input an integer number: ");
            }
        }

        LocalDate endDate = startDate.plusDays(daysCount);

        double bill = generateBill(daysCount, rooms.get(roomIndex));
        System.out.println("\nYou need to pay " + bill);
        boolean exit = false;
        while (!exit) {
            System.out.println("Do you confirm reserving?");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    Booked booked = new Booked(customers.get(customerIndex), rooms.get(roomIndex), startDate, endDate, bill);
                    bookingsInformation.add(booked);
                    customers.get(customerIndex).addBookingInformation(booked);
                    rooms.get(roomIndex).markAsNotAvailable();
                    exit = true;
                }
                case "2" -> {
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Booking on behalf of administrator
    public void bookingByAdministrator() {
        displayOrAddCustomersIfNecessary();

        int customerId = -1;
        int customerIndex = -1;
        boolean exit = false;
        while (!exit) {
            System.out.print("\nChoose ID of customer who want to booking room: ");

            while (customerId == -1) {
                try {
                    customerId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.print("\nPlease input integer number: ");
                }
            }

            customerIndex = findCustomerByID(customerId);
            if (customerIndex != -1) {
                exit = true;
            } else {
                System.out.println("Wrong ID, try again.");
                customerId = -1;
            }
        }

        booking(customerIndex);
    }

    // Booking on behalf of customer
    public void bookingByCustomer(int id) {
        int customerIndex = findCustomerByID(id);

        booking(customerIndex);
    }

    // If the number has already been released, puts the appropriate mark
    public void checkReservedRooms() {
        LocalDate currentDate = LocalDate.now();

        for (Booked booked : bookingsInformation) {
            if (booked.getEndDate().isBefore(currentDate)) {
                booked.getRoom().markAsAvailable();
            }
        }
    }

    // For filling start date
    private LocalDate inputStartDate() {
        String startDateString = scanner.nextLine();

        LocalDate startDate = null;

        boolean exit = false;
        while (!exit) {
            try {
                startDate = LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                exit = true;
            } catch (Exception e) {
                System.out.print("\nPlease input date in 'dd-MM-yyyy' format: ");
                startDateString = scanner.nextLine();
            }
        }

        return startDate;
    }

    // Writes information about bookings at the hotel in a file
    public void writeCustomerInfo() {
        new History("HotelBookingsInfo.txt", bookingsInformation);
    }
}
