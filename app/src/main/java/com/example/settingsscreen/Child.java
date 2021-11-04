package com.example.settingsscreen;

public class Child {

    String id, fName, childClass, school, childPic, admNo;

    public Child(String id, String fName, String childClass, String school,String childPic, String admNo){

        this.id = id;
        this.fName = fName;
        this.childClass = childClass;
        this.school = school;
        this.childPic = childPic;
        this.admNo = admNo;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getChildPic() {
        return childPic;
    }

    public void setChildPic(String childPic) {
        this.childPic = childPic;
    }

    public String getAdmNo() {
        return admNo;
    }

    public void setAdmNo(String admNo) {
        this.admNo = admNo;
    }
}
