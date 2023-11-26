package com.example.courseprojectnetology.controller;

import com.example.courseprojectnetology.models.FilePlace;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cloud")
public interface FileLoadController {

    @PostMapping("/file")
    ResponseEntity<String> uploadFileToServer(
            @RequestParam("name") String fileName,
            @RequestPart("content") MultipartFile multipartFile);

    @DeleteMapping("/file")
    ResponseEntity<String> deleteFile(
            @RequestParam("name") String fileName
    );

    @GetMapping("/file")
    ResponseEntity<Resource> downloadFileFromCloud(
            @RequestParam("name") String fileName
    );

    @PutMapping("/file")
    ResponseEntity<String> editFileName(
            @RequestParam("name") String fileName,
            @RequestBody String newFileName
    );

    @GetMapping("/list")
    ResponseEntity<List<FilePlace>> getAllFiles();
}
