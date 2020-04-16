package com.example.projectphoenix;

public class Tournament {
    private String title;
    private String date;
    private String time;
    private int maxNumPlayers;
    private String description;

    public Tournament() {
        //Empty constructor for Firebase
    }

    public Tournament(String titleIn, String dateIn, String timeIn, int maxNumPlayersIn, String descriptionIn) {
        this.title = titleIn;
        this.date = dateIn;
        this.time = timeIn;
        this.maxNumPlayers = maxNumPlayersIn;
        this.description = descriptionIn;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getMaxNumPlayers() {
        return maxNumPlayers;
    }

    public String getDescription() {
        return description;
    }
}
