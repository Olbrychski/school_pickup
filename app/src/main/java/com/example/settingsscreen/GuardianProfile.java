package com.example.settingsscreen;

public class GuardianProfile {

    private String guardianName;
    private String guardianTitle;
    private String guardianPhoneNumber;
    private String guardianEmail;
    private int guardianProfileImage;

    public GuardianProfile(String guardianName, String guardianTitle, String guardianPhoneNumber, String guardianEmail, int guardianProfileImage) {
        this.guardianName = guardianName;
        this.guardianTitle = guardianTitle;
        this.guardianPhoneNumber = guardianPhoneNumber;
        this.guardianEmail = guardianEmail;
        this.guardianProfileImage = guardianProfileImage;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianTitle() {
        return guardianTitle;
    }

    public void setGuardianTitle(String guardianTitle) {
        this.guardianTitle = guardianTitle;
    }

    public String getGuardianPhoneNumber() {
        return guardianPhoneNumber;
    }

    public void setGuardianPhoneNumber(String guardianPhoneNumber) {
        this.guardianPhoneNumber = guardianPhoneNumber;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    public int getGuardianProfileImage() {
        return guardianProfileImage;
    }

    public void setGuardianProfileImage(int guardianProfileImage) {
        this.guardianProfileImage = guardianProfileImage;
    }
}
