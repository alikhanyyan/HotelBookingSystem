import java.io.Serializable;
import java.util.ArrayList;

class Room implements Serializable {
    private final int roomId;
    private final RoomType roomType;
    private final double pricePerDay;
    private boolean isAvailable;
    private final ArrayList<String> whatHave;

    public Room(RoomType roomType) {
        this.roomType = roomType;
        this.pricePerDay = getPricePerDay();
        whatHave = new ArrayList<>();
        isAvailable = true;
        setWhatHave();
        roomId = System.identityHashCode(this);
    }

    public int getRoomId() {
        return roomId;
    }
    public RoomType getRoomType() {
        return roomType;
    }
    public double getPricePerDay() {
        return switch (roomType)
        {
            case SINGLE -> 20;
            case DOUBLE -> 35;
            case DELUXE -> 55;
        };
    }
    public boolean checkIsAvailable() {
        return isAvailable;
    }
    public ArrayList<String> getWhatHave() {
        return new ArrayList<>(whatHave);
    }

    private void setWhatHave()
    {
        switch (roomType)
        {
            case SINGLE -> {
                whatHave.add("single bed");
                whatHave.add("bathroom");
                whatHave.add("TV");
                whatHave.add("closet");
            }
            case DOUBLE -> {
                whatHave.add("double bed");
                whatHave.add("bathroom");
                whatHave.add("TV");
                whatHave.add("closet");
            }
            case DELUXE -> {
                whatHave.add("minibar");
                whatHave.add("bathtub");
                whatHave.add("king-size bed");
                whatHave.add("sitting area");
            }
        }
    }

    public void markAsAvailable() {
        isAvailable = true;
    }
    public void markAsNotAvailable() {
        isAvailable = false;
    }

    @Override
    public String toString() {
        return String.format("Room{room ID=%d, is room available=%s, room type=%s, price per day=%.2f, what room have=%s}",
                roomId, isAvailable, roomType, pricePerDay, whatHave);

    }
}