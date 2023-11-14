import java.io.Serializable;
import java.time.LocalDate;

class Booked implements Serializable {
    private final Room room;
    private final Customer customer;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double bill;

    public Booked(Customer customer, Room room, LocalDate startDate, LocalDate endDate, double bill) {
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bill = bill;
    }

    public Room getRoom() {
        return room;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return String.format("Booked{%n" +
                        "     room=[room ID=%s, room type=%s, what room have=%s],%n" +
                        "     customer=[customer ID=%s, name='%s', email='%s'],%n" +
                        "     start date=%s, end date=%s, bill=%s}",
                room.getRoomId(), room.getRoomType(), room.getWhatHave(),
                customer.getId(), customer.getName(), customer.getEmail(),
                startDate, endDate, bill);
    }
}
