import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class History {
    private final String fileName;
    //solid
    private final ArrayList<Booked> bookingInformation;

    public History(String fileName, ArrayList<Booked> bookingInformation) {
        this.fileName = fileName;
        this.bookingInformation = new ArrayList<>(bookingInformation);
        writeInFile();
    }

    // Writes history
    private void writeInFile() {
        File file = new File(fileName);

        try (FileWriter fw = new FileWriter(file)) {
            for (Booked info : bookingInformation) {
                fw.write(info.toString() + '\n');
            }
            System.out.println("Successfully write to file.");
        }  catch (IOException e) {
            System.out.println("Couldn't save the data to a file" + fileName);
        }
    }
}
