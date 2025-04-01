package com.example.library.Requestuserinfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;


@Getter
@Setter

@ToString
@AllArgsConstructor
@NoArgsConstructor

public class Signpacket {
        private String username;
        private String firstname;
        private String lastname;
        private String emailid;
        private String role;
        private String password;
        private String key;

}
