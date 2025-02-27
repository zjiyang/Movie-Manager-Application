package persistence;

import org.json.JSONObject;

// interface class for reader and writer to use
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}