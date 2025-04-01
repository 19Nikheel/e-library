package com.example.library.Fileuploadhelper;

import com.example.library.Entity.Book;
import com.example.library.Service.Repo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class Fileuploader {
    String uploadfile = "upload";

    @Autowired
    private Repo Bookrepo;

    public boolean fiupload(MultipartFile file,String name) {
        boolean b=false;
//        File fil = new File(uploadfile);
        Path filepath= Paths.get(uploadfile+File.separator+"pdf_upload");
        File fil=filepath.toFile();
        if (!fil.exists()) {
            fil.mkdirs();
        }
        try {

            filepath=Paths.get(uploadfile+File.separator+"pdf_upload").resolve(name+file.getOriginalFilename());
            file.transferTo(filepath);
            String imagePath = extractFirstPage(filepath.toString());
            Book b1=new Book();
            b1.setName(file.getOriginalFilename().substring(0,file.getOriginalFilename().length()-4));
            b1.setAuthor(name);
            b1.setSize(file.getSize());
            b1.setImagepath(imagePath);
            b1.setFilepath(filepath.toString());
            Bookrepo.save(b1);
            b=true;
            return b;
        } catch (IOException e) {
            System.out.println(e.fillInStackTrace());
            return b;
        }


    }

    public boolean fieditupload(MultipartFile file,Book b1) {
        boolean b=false;

        try {
            Path filepath=Paths.get(uploadfile+File.separator+"pdf_upload").resolve(b1.getAuthor()+file.getOriginalFilename()).normalize();
            file.transferTo(filepath);
            String imagePath = extractFirstPage(filepath.toString());
            b1.setImagepath(imagePath);
            Path dpath=Paths.get(b1.getFilepath()).normalize();
            File fd=dpath.toFile();
            fd.delete();
            b1.setFilepath(filepath.toString());
            Bookrepo.save(b1);
            b=true;
            return b;
        } catch (IOException e) {
            System.out.println(e.fillInStackTrace());
            return b;
        }


    }

    private String extractFirstPage(String pdfPath) throws IOException {
        File file = new File(pdfPath);
        if (!file.exists()) throw new IOException("PDF not found");

        PDDocument document = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI(0, 150, ImageType.RGB);
        document.close();

        Path filepath= Paths.get(uploadfile+File.separator+"image_upload");
        File fil=filepath.toFile();
        if (!fil.exists()) {
            fil.mkdirs();
        }

        Path image_upload = Paths.get(uploadfile+File.separator+"image_upload");
        if (!Files.exists(image_upload)) Files.createDirectories(image_upload);

        String imagePath=Paths.get(uploadfile+File.separator+"image_upload").resolve(file.getName().replace(".pdf", ".png")).toString();
        System.out.println(imagePath);
        ImageIO.write(image, "PNG", new File(imagePath));
        return imagePath;
    }

}
