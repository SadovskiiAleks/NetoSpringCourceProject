package com.example.courseprojectnetology.service;

import com.example.courseprojectnetology.models.FilePlace;
import com.example.courseprojectnetology.models.NewFileName;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface FileService {

    ResponseEntity<String> uploadFileToServer(String fileName,
                                              MultipartFile multipartFile);

    ResponseEntity<String> deleteFile(String name);

    ResponseEntity<Resource> downloadFileFromCloud(String name);

    ResponseEntity<String> editFileName(String name, NewFileName newFileName);

    ResponseEntity<List<FilePlace>> getAllFiles();

}
