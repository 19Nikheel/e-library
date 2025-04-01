package com.example.library.Entity;


import lombok.*;
import org.springframework.stereotype.Component;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class Bookresponce {

    String name;
    String author;
    String imagename;
}
