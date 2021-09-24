package com.example.settingsscreen;

public class myChildrenItem {
    private int childImage;
    private String childName;
    private String childClass;
    private String childLocation;

    public myChildrenItem(int childImage, String childName, String childClass, String childLocation) {
        this.childImage = childImage;
        this.childName = childName;
        this.childClass = childClass;
        this.childLocation = childLocation;

    }

    public int getChildImage() {
        return childImage;
    }

    public void setChildImage(int childImage) {
        this.childImage = childImage;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildClass() {
        return childClass;
    }

    public void setChildClass(String childClass) {
        this.childClass = childClass;
    }

    public String getChildLocation() {
        return childLocation;
    }

    public void setChildLocation(String childLocation) {
        this.childLocation = childLocation;
    }
}
