package com.example.library.Controller;


import com.example.library.Entity.Book;
import com.example.library.Entity.Bookresponce;
import com.example.library.Fileuploadhelper.Fileuploader;
import com.example.library.Service.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private Fileuploader fup;

    @Autowired
    private Repo bookrepo;



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/post")
    public ResponseEntity<String> fun1(@RequestParam("file") MultipartFile file , @RequestParam("name") String name){

        if(file.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if(!file.getContentType().equals("application/pdf")){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

            boolean b= fup.fiupload(file,name);
            if(b){

                return ResponseEntity.ok("File uploaded successfully: ");
            }else {
                
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
    }

    @GetMapping("/find/{name}")
    public ResponseEntity<?> func2(@PathVariable("name") String name){
        List<Book> b1= bookrepo.findByNameContainingIgnoreCase(name);
        List<Book> b3=bookrepo.findByAuthorContainingIgnoreCase(name);
        Set<Book> set1=new HashSet<>(b1);
        set1.addAll(b3);
        b1=new ArrayList<>(set1);
        if(b1.size()==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Book  '%s'  not found",name));
        }
        List<Bookresponce> bookres=new ArrayList<>();
        for(Book b:b1){
            bookres.add(new Bookresponce(b.getName(),b.getAuthor(),Paths.get(b.getImagepath()).getFileName().toString()));
        }
        return ResponseEntity.ok(bookres);
    }

    @GetMapping("/findall")
    public ResponseEntity<?> func6(){
        List<Book> b1= bookrepo.findAll();
        System.out.println(b1.size());
        if(b1.size()==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("No Book available"));
        }
        List<Bookresponce> bookres=new ArrayList<>();
        for(Book b:b1){
            bookres.add(new Bookresponce(b.getName(),b.getAuthor(),Paths.get(b.getImagepath()).getFileName().toString()));
        }
        return ResponseEntity.ok(bookres);
    }

    @GetMapping("/find")
    public ResponseEntity<?> func3(@RequestParam String name,@RequestParam String author){
        name=name.replace("%20"," ");
        author=author.replace("%20"," ");

        Book b1= bookrepo.findByNameAndAuthor(name,author);

        System.out.println(34);
        if(b1==null){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Book  '%s'  not found",name));
        }
        System.out.println(354);
        Path f1=Paths.get(b1.getFilepath());
        try {
            Resource resource=new UrlResource(f1.toUri());
            if(!resource.exists()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Book  '%s'  not found",name));
            }

            System.out.println(349);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+resource.getFilename())
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong with resources");
        }

    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit")
    public ResponseEntity<?> func4(@RequestParam String name,@RequestParam String author,@RequestParam("file") MultipartFile file){
        name=name.replace("%20"," ");
        author=author.replace("%20"," ");
        Book b1= bookrepo.findByNameAndAuthor(name,author);
        if(b1==null){
            System.out.println(12);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Book  '%s'  not found",name));
        }
        Path filePath = Paths.get(b1.getFilepath()).normalize();
        Path imagePath = Paths.get(b1.getImagepath()).normalize();
        File ip=imagePath.toFile();

        File fp=filePath.toFile();
        if(!fp.exists() || !ip.exists()){
            System.out.println(44);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Book  '%s'  not found",name));
        }
        boolean n=fup.fieditupload(file,b1);
        if(n){
            fp.delete();
            ip.delete();
            return ResponseEntity.ok().body("Success fully updated");
        }else {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something is wrong we are looking it in");
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> func5(@RequestParam String name,@RequestParam String author){
        name=name.replace("%20"," ");
        author=author.replace("%20"," ");
        Book b1= bookrepo.findByNameAndAuthor(name,author);
        if(b1==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Book  '%s'  not found",name));
        }
        Path filePath = Paths.get(b1.getFilepath()).normalize();
        Path imagePath = Paths.get(b1.getImagepath()).normalize();
        File fp=filePath.toFile();
        File ip=imagePath.toFile();
        if(!fp.exists() || !ip.exists()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("file does not exist");
        }
        ip.delete();
        fp.delete();
        bookrepo.deleteById(b1.getId());

        return ResponseEntity.ok().body("deleted successfully");
    }
}
