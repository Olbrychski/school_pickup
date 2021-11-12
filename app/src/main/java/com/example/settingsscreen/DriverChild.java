package com.example.settingsscreen;

public class DriverChild {

    private String childPic, fName, childPickLocationLong, childClass, childPickLocationLat, admNo;
    private int id;

    public DriverChild(int id, String childPic, String fName, String childPickLocationLong, String childClass, String childPickLocationLat, String admNo){

        this.id = id;
        this.childPic = childPic;
        this.fName = fName;
        this.childPickLocationLong = childPickLocationLong;
        this.childClass = childClass;
        this.childPickLocationLat = childPickLocationLat;
        this.admNo = admNo;


    }

    public String getChildPic() {
        return childPic;
    }

    public void setChildPic(String childPic) {
        this.childPic = childPic;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getChildClass() {
        return childClass;
    }

    public void setChildClass(String childClass) {
        this.childClass = childClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmNo() {
        return admNo;
    }

    public void setAdmNo(String admNo) {
        this.admNo = admNo;
    }

    public String getChildPickLocationLong() {
        return childPickLocationLong;
    }

    public void setChildPickLocationLong(String childPickLocationLong) {
        this.childPickLocationLong = childPickLocationLong;
    }

    public String getChildPickLocationLat() {
        return childPickLocationLat;
    }

    public void setChildPickLocationLat(String childPickLocationLat) {
        this.childPickLocationLat = childPickLocationLat;
    }
}
