package com.example.library.Service;

import com.example.library.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface Repo extends JpaRepository<Book,Long> {

     List<Book> findByNameContainingIgnoreCase(String name);


    List<Book> findByAuthorContainingIgnoreCase(String n);

    public Book findByNameAndAuthor(String n,String m);

    @Query("SELECT b.filepath FROM Book b")
    List<String> findAllFilepath();

}
