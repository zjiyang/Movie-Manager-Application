package model;

// Represent a rate with score (0-5) from a userID 
public class Rate {
    int userID;
    int score;

    // REQUIRES: userID > 0; 0 <= score <= 5, 
    // EFFECTS: contruct a score with given userID and score.
    public Rate(int userID, int score) {
        this.userID = userID;
        this.score = score;
    }

    // below are getters
    public int getUserID() {
        return this.userID;
    }

    public int getScore() {
        return this.score;
    }

    // Define 5 Rate for userID 001, as public static final objects for test
    public static final Rate rate0 = new Rate(001,0);
    public static final Rate rate1 = new Rate(001,1);
    public static final Rate rate2 = new Rate(001,2);
    public static final Rate rate3 = new Rate(001,3);
    public static final Rate rate4 = new Rate(001,4);
    public static final Rate rate5 = new Rate(001,5);
    public static final Rate rate6 = new Rate(001,6);
    public static final Rate rate7 = new Rate(001,7);
    public static final Rate rate8 = new Rate(001,8);
    public static final Rate rate9 = new Rate(001,9);
    public static final Rate rate10 = new Rate(001,10);

}
