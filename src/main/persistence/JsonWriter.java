package persistence;

// Represents a writer that writes JSON representation of MovieDataBase to file
public class JsonWriter {

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {

    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() {
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of MovieDataBase to file
    public void write() {
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    public void saveToFile() {
    }
}
