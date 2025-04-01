package com.example.library.Service;

import com.example.library.Entity.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public interface Userrepo extends JpaRepository<Userinfo,Long> {
    Optional<Userinfo> findByUsername(String username);
    Optional<Userinfo> findByEmailid(String email);
}
