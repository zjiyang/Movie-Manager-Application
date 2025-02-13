package model;

//Represents Stream Platform for watch movie online
public class StreamPlatform {
    private String name;

    public StreamPlatform(String name) {
        this.name = name;
    }

    public String getStreamPlatformName() {
        return this.name;
    }
    
    // Define commonly used genres as public static final objects
    public static final StreamPlatform Netflix = new StreamPlatform("Netflix");
    public static final StreamPlatform DisneyPlus = new StreamPlatform("DisneyPlus");
    public static final StreamPlatform AppleTV = new StreamPlatform("AppleTV");
    public static final StreamPlatform PrimeVideo = new StreamPlatform("PrimeVideo");

}
