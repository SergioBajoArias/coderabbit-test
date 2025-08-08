package com.sergio;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public String getGreetingUppercase() {
        String name = null;
        // NullPointerException
        return name.toUpperCase();
    }

    public String getGreetingUppercase(String userName) {
        // SQL injection vulnerability
        String query = "SELECT * FROM users WHERE name = '" + userName + "'";

        return userName.toUpperCase();
    }

    public String getGreetingUppercase(String userName, String userFirstname, String userLastName) {
        // Concat strings directly instead of by means of StringBuilder
        return userName + " " + userFirstname + " " + userLastName;
    }
}
