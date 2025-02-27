package ui;

import java.io.FileNotFoundException;

// Launch the app and catch the file not found exception 
public class Main {
    public static void main(String[] args) throws Exception {
        try {
            new MovieApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}