package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/s3")
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @PostMapping("upload")
    public ResponseEntity<List<String>> upload(@RequestParam("file") List<MultipartFile> files) throws IOException {
        return s3Service.saveFile(files);
    }

    @GetMapping("download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable("filename") String filename){
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-type", MediaType.ALL_VALUE);
        headers.add("Content-Disposition", "attachment; filename="+filename);
        byte[] bytes = s3Service.downloadFile(filename);
        return  ResponseEntity.status(HTTP_OK).headers(headers).body(bytes);
    }


    @DeleteMapping("delete/{filename}")
    public  String deleteFile(@PathVariable("filename") String filename){
        return s3Service.deleteFile(filename);
    }

    @GetMapping("list")
    public List<String> getAllFiles(){

        return s3Service.listAllFiles();

    }
}
