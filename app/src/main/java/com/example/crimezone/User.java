package com.example.crimezone;
import java.io.Serializable;

public class User implements Serializable {

    public User(String fname, String lname, String email, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
    }

    public String getfname() {
        return fname;
    }

    public void setfname(String val) {
        this.fname = val;
    }

    public String getlname() {
        return lname;
    }

    public void setlname(String val) {
        this.lname = val;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String val) {
        this.email = val;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String val) {
        this.phone = val;
    }

    private String fname;
    private String lname;
    private String email;
    private String phone;

}
