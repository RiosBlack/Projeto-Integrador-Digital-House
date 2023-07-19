package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.service.S3Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class S3ControllerTest {

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private S3Controller s3Controller;

    @Test
    public void shouldReturnUploadedFileName() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Test file".getBytes());
        List<MultipartFile> files = new ArrayList<>();
        files.add(file);

        List<String> expectedUrls = Arrays.asList("https://buketnName.s3.us-east-2.amazonaws.com/test.txt");

        when(s3Service.saveFile(files)).thenReturn(ResponseEntity.ok().body(expectedUrls));

        ResponseEntity<List<String>> result = s3Controller.upload(files);
        List<String> resultUrls = result.getBody();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedUrls, resultUrls);
    }

    @Test
    public void shouldDownloadAFile() throws Exception {
        String filename = "test.txt";
        byte[] fileContent = "Test file".getBytes();

        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.add("Content-type", MediaType.ALL_VALUE);
        expectedHeaders.add("Content-Disposition", "attachment; filename=" + filename);
        
        ResponseEntity<byte[]> expectedResponse = ResponseEntity.status(HttpStatus.OK).headers(expectedHeaders).body(fileContent);
        
        when(s3Service.downloadFile(filename)).thenReturn(fileContent);

        ResponseEntity<byte[]> result = s3Controller.download(filename);

        assertEquals(expectedResponse.getStatusCode(), result.getStatusCode());
        assertEquals(expectedResponse.getHeaders(), result.getHeaders());
        assertArrayEquals(expectedResponse.getBody(), result.getBody());
    }

    @Test
    public void shouldDeleteFile() {
        String filename = "test.txt";
        String expectedMessage = "File deleted successfully";

        when(s3Service.deleteFile(filename)).thenReturn(expectedMessage);

        String result = s3Controller.deleteFile(filename);

        assertEquals(expectedMessage, result);
    }

    @Test
    public void shouldGetAListOfAllFiles() {
        List<String> expectedFiles = Arrays.asList("file1.txt", "file2.txt", "file3.txt");

        when(s3Service.listAllFiles()).thenReturn(expectedFiles);

        List<String> result = s3Controller.getAllFiles();

        assertEquals(expectedFiles, result);
    }
}

