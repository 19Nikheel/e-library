package com.example.library.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Builder
@Getter
@Setter
@AllArgsConstructor
public class JwtRequest {
    private String username;
    private String password;

    public JwtRequest() {
    }



}
