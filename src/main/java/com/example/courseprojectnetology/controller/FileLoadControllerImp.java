package com.example.courseprojectnetology.controller;

import com.example.courseprojectnetology.models.FilePlace;
import com.example.courseprojectnetology.models.NewFileName;
import com.example.courseprojectnetology.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class FileLoadControllerImp implements FileLoadController {
    @Autowired
    FileService fileService;


    @Override
    public ResponseEntity<String> uploadFileToServer(String fileName, MultipartFile multipartFile) {
        ResponseEntity<String> responseEntity = fileService.uploadFileToServer(fileName, multipartFile);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> deleteFile(String fileName) {
        ResponseEntity<String> responseEntity = fileService.deleteFile(fileName);
        return responseEntity;
    }

    @Override
    public ResponseEntity<Resource> downloadFileFromCloud(String fileName) {
        ResponseEntity<Resource> responseEntity = fileService.downloadFileFromCloud(fileName);
        return responseEntity;
    }

    @Override
    public ResponseEntity<String> editFileName(String fileName, NewFileName newFileName) {
        ResponseEntity<String> responseEntity = fileService.editFileName(fileName, newFileName);
        return null;
    }

    @Override
    public ResponseEntity<List<FilePlace>> getAllFiles() {
        ResponseEntity<List<FilePlace>> responseEntity = fileService.getAllFiles();
        return responseEntity;
    }
}
