package com.example.projectphoenix;

public class DevApplication {
    String email;
    String franchise;
    String platform;
    public DevApplication() {

    }

    public DevApplication(String emailIn, String franchiseIn, String platformIn) {
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
