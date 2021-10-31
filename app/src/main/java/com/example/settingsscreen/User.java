package com.example.settingsscreen;

public class User {

    private int id;
    private String fName, idNo, phoneNumber, emailAddress, role, carNo, driverPic;

    public User(int id, String fName, String idNo, String phoneNumber, String emailAddress, String role, String carNo, String driverPic) {
        this.id = id;
        this.fName = fName;
        this.idNo = idNo;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.role = role;
        this.carNo = carNo;
        this.driverPic = driverPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getDriverPic() {
        return driverPic;
    }

    public void setDriverPic(String driverPic) {
        this.driverPic = driverPic;
    }

}
