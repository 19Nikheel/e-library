package com.example.library.Entity;


import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class JwtResponce {

    private String Jwttoken;
    private String username;

    public String getJwttoken() {
        return Jwttoken;
    }

    @Override
    public String toString() {
        return "JwtResponce{" +
                "Jwttoken='" + Jwttoken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public JwtResponce() {
    }

    public JwtResponce(String jwttoken, String username) {
        Jwttoken = jwttoken;
        this.username = username;
    }

    public void setJwttoken(String jwttoken) {
        Jwttoken = jwttoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
