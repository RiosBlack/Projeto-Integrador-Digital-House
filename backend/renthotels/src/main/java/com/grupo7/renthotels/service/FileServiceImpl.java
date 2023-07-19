package com.grupo7.renthotels.service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileServiceImpl {

    ResponseEntity<List<String>> saveFile(List<MultipartFile> files) throws IOException;

    byte[] downloadFile(String filename);


    String deleteFile(String filename);


    List<String> listAllFiles();
}
