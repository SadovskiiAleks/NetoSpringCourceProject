package com.example.courseprojectnetology.controller.Impl;

import com.example.courseprojectnetology.controller.FileLoadController;
import com.example.courseprojectnetology.dto.FileDTO;
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
    private final FileService fileService;

    @Autowired
    public FileLoadControllerImp(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public String uploadFileToServer(String fileName, MultipartFile multipartFile) {
        return fileService.uploadFileToServer(fileName, multipartFile);
    }

    @Override
    public String deleteFile(String fileName) {
        return fileService.deleteFile(fileName);
    }

    @Override
    public FileDTO downloadFileFromCloud(String fileName) {
        return fileService.downloadFileFromCloud(fileName);
    }

    @Override
    public String editFileName(String fileName, NewFileName newFileName) {
        return fileService.editFileName(fileName, newFileName);
    }

    @Override
    public List<FilePlace> getAllFiles() {
        return fileService.getAllFiles();
    }
}
