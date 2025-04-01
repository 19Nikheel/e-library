package com.example.library.config;

import com.example.library.Entity.Userinfo;
import com.example.library.Service.Userrepo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private Userrepo userrepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Userinfo user = userrepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole()) // Default role
                    .build();
        }


}
