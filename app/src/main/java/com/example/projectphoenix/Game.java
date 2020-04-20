package com.example.projectphoenix;

public class Game {
    private String gameFranchise;
    private String gamePlatform;
    private String name;

    //Empty constructor for Firebase
    public Game() {

    }

    public Game(String franchiseIn, String platformIn, String nameIn) {
        this.gameFranchise = franchiseIn;
        this.gamePlatform = platformIn;
        this.name = nameIn;
    }

    public String getGameFranchise() {
        return gameFranchise;
    }

    public String getGamePlatform() {
        return gamePlatform;
    }


    public String getName() {
        return name;
    }
}
