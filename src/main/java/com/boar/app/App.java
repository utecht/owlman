package com.boar.app;
import static spark.Spark.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;

public class App {
    public static void main(String[] args) {
        staticFiles.location("/public");

        File uploadDir = new File("upload");
        uploadDir.mkdir();
        staticFiles.externalLocation("upload");

        get("/hello", (request, response) -> {
          return "Hello World";
        });

        post("/rdf_upload", (request, response) -> {
          Path tempFile = Files.createTempFile(uploadDir.toPath(), "", "");
          request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

          try(InputStream is = request.raw().getPart("uploaded_file").getInputStream()){
            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
          }

          //clean up uploaded file
          return "file upload";
        });
    }
}
