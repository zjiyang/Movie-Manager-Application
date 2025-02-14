package model;

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
}
