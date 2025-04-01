package com.example.library.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor



public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String name;

    private String author;
    private String filepath;
    private String imagepath;
    private long size;
    private LocalDateTime upload_time=LocalDateTime.now();


}
