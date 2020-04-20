package com.example.projectphoenix;

import java.util.ArrayList;

/**
 * Tournament:
 * Data class that holds all the information for the Tournament object.
 */
public class Tournament {
    private String title;
    private String gameTitle;
    private String date;
    private String time;
    private int maxNumPlayers;
    private int currNumPlayers;
    private String description;
    private ArrayList<String> players;

    public Tournament() {
        //Empty constructor for Firebase
    }

    public Tournament(String titleIn, String gameTitleIn, String dateIn, String timeIn, int maxNumPlayersIn, String descriptionIn) {
        this.title = titleIn;
        this.gameTitle = gameTitleIn;
        this.date = dateIn;
        this.time = timeIn;
        this.maxNumPlayers = maxNumPlayersIn;
        this.currNumPlayers = 0;
        this.description = descriptionIn;
        this.players = new ArrayList<>(maxNumPlayersIn);
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public boolean containsPlayer(String emailIn) {
        return players.contains(emailIn);
    }

    public int getCurrNumPlayers() {
        return currNumPlayers;
    }

    public void setCurrNumPlayers(int currNumPlayers) {
        this.currNumPlayers = currNumPlayers;
    }

    public void addPlayer(String emailIn) {
        players.add(emailIn);
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
