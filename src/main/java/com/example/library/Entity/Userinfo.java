package com.example.library.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.stereotype.Component;


@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor


public class Userinfo {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    private String username;
    private String firstname;
    private String lastname;
    private String emailid;
    private String role;
    private String password;
}
