package ui;

import java.io.FileNotFoundException;

// launch the app
public class Main {
    public static void main(String[] args) throws Exception {
        try {
            new MovieApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}