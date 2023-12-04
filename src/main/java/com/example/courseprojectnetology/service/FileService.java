package com.example.courseprojectnetology.service;

import com.example.courseprojectnetology.dto.FileDTO;
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

    String uploadFileToServer(String fileName,
                                              MultipartFile multipartFile);

    String deleteFile(String name);

    FileDTO downloadFileFromCloud(String name);

    String editFileName(String name, NewFileName newFileName);

    List<FilePlace> getAllFiles();

}
