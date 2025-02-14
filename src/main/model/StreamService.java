package model;

//Represents Stream Platform for watch movie online
public class StreamService {
    private String name;

    // EFFECTS: Construct a movie stream service provider with given name.
    public StreamService(String name) {
        this.name = name;
    }

    public String getStreamPlatformName() {
        return this.name;
    }


    //Define commonly used StreamService as public static final objects
    public static final StreamService Netflix = new StreamService("Netflix");
    public static final StreamService DisneyPlus = new StreamService("DisneyPlus");
    public static final StreamService AppleTV = new StreamService("AppleTV");
    public static final StreamService PrimeVideo = new StreamService("PrimeVideo");

}
