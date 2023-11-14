import java.io.*;
import java.util.*;

public class HotelBookingSystem {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Hotel hotel = loadDatabase();

        Thread checking = new Thread(() -> {
            while (true) {
                hotel.checkReservedRooms();
            }
        });
        checking.setDaemon(true);
        checking.start();

        System.out.println("\nWelcome to Advanced Hotel Room Booking System!");

        boolean exit = false;
        while (!exit) {
            System.out.println("\nDo you hotel administrator or customer?");
            System.out.println("1. Administrator");
            System.out.println("2. Customer");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("\nEnter password: ");
                    String password = scanner.nextLine();
                    if (!password.equals(Hotel.PASSWORD)) {
                        System.out.println("Wrong password, permission denied.");
                    } else {
                        administratorPermissions(hotel);
                    }
                }
                case "2" -> customerPermissions(hotel);
                case "0" -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }

            saveDatabase(hotel);
        }
    }

    // Login with hotel administrator permissions
    private static void administratorPermissions(Hotel hotel) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\nAdministrator permissions:");
            System.out.println("1. Display registered customers");
            System.out.println("2. Add new customer");
            System.out.println("3. Display rooms");
            System.out.println("4. Add new room");
            System.out.println("5. Booking");
            System.out.println("6. Save database");
            System.out.println("7. Write info about bookings at the hotel in a HotelBookingsInfo.txt file");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> hotel.displayOrAddCustomersIfNecessary();
                case "2" -> hotel.addCustomer();
                case "3" -> hotel.displayOrAddRoomsIfNecessary();
                case "4" -> hotel.addRoom();
                case "5" -> hotel.bookingByAdministrator();
                case "6" -> saveDatabase(hotel);
                case "7" -> hotel.writeCustomerInfo();
                case "0" -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Login with customer permissions
    private static void customerPermissions(Hotel hotel) {
        System.out.print("\nEnter your name: ");
        String name = scanner.nextLine();
        while (name.isEmpty()) {
            name = scanner.nextLine();
        }

        System.out.print("Enter you contact email: ");
        String email = scanner.nextLine();
        while (email.isEmpty()) {
            email = scanner.nextLine();
        }

        Customer customer = hotel.getCustomerId(name, email);

        boolean exit = false;
        if (customer == null) {
            while (!exit) {
                System.out.println("\nYou don't registered. Do you want to register?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print("Enter your choice: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> {
                        customer = hotel.registration(name, email);
                        exit = true;
                    }
                    case "2" -> {
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        exit = false;
        while (!exit) {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Reserve room");
            System.out.println("2. Write information about bookings in file");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> hotel.bookingByCustomer(customer.getId());
                case "2" -> customer.writeCustomerInfo();
                case "0" -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    // Saves hotel database
    private static void saveDatabase(Hotel hotel) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Hotel.DATABASE_FILE))) {
            oos.writeObject(hotel);
            System.out.println("Hotel database saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving hotel database: " + e.getMessage());
        }
    }

    // Loads hotel database
    private static Hotel loadDatabase() {
        Hotel hotel;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Hotel.DATABASE_FILE))) {
            hotel = (Hotel) ois.readObject();
            System.out.println("Hotel database loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Hotel database file not found. Starting with an empty database.");
            hotel = new Hotel();
        }

        return hotel;
    }
}
