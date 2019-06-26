package com.example.crimezone;
import com.example.crimezone.User;

public class APITesting {

    public User getUser(){
        //assume we will have parameters that will be the form from login. Send it to this function and retrieve it from firebase.
        //Depending on the reply from firebase, return 0 on failure or 1 on success. Assume always success for developing and testing purposes.
        User temp = new User("Donton", "Zhong", "zhongcan00@gmail.com", "2623276654");
        return temp;

    }
}
