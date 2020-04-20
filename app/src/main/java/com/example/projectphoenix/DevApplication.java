package com.example.projectphoenix;
/**
 * DevApplication:
 *       Data class that holds data for the DeveloperApplication object
 */
public class DevApplication {

    private String email;
    private String franchise;
    private String platform;

    public DevApplication() {
        //Empty constructor required for database.
    }

    DevApplication(String emailIn, String franchiseIn, String platformIn) {
        this.email = emailIn;
        this.franchise = franchiseIn;
        this.platform = platformIn;
    }

    public String getEmail() {
        return email;
    }

    public String getFranchise() {
        return franchise;
    }

    public String getPlatform() {
        return platform;
    }
}
