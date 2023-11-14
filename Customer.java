import java.io.Serializable;
import java.util.ArrayList;

class Customer implements Serializable {
    private final String name;
    private final String email;
    private final int id;
    private final ArrayList<Booked> bookingsInformation = new ArrayList<>();

    public Customer(String name, String email) {
        id = System.identityHashCode(this);
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public int getId() {
        return id;
    }


    // Adds booking information
    public void addBookingInformation(Booked booked) {
        bookingsInformation.add(booked);
    }

    // Writes information about the client's bookings to a file
    public void writeCustomerInfo() {
        new History(name + id + ".txt", bookingsInformation);
    }

    @Override
    public String toString() {
        return String.format("Customer{ID=%d, name='%s', email='%s'}",
                id, name, email);
    }
}
