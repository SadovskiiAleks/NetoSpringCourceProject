package com.example.courseprojectnetology.controller;

import com.example.courseprojectnetology.dto.FileDTO;
import com.example.courseprojectnetology.models.FilePlace;
import com.example.courseprojectnetology.models.NewFileName;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/cloud")
public interface FileLoadController {

    @PostMapping("/file")
    String uploadFileToServer(
            @RequestParam("name") String fileName,
            @RequestPart("content") MultipartFile multipartFile);

    @DeleteMapping("/file")
    String deleteFile(
            @RequestParam("name") String fileName
    );

    @GetMapping("/file")
    FileDTO downloadFileFromCloud(
            @RequestParam("name") String fileName
    );

    @PutMapping("/file")
    String editFileName(
            @RequestParam("name") String fileName,
            @RequestBody NewFileName newFileName
    );

    @GetMapping("/list")
    List<FilePlace> getAllFiles();
}
