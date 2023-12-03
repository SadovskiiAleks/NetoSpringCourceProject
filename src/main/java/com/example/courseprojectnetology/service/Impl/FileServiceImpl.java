package com.example.courseprojectnetology.service.Impl;

import com.example.courseprojectnetology.exception.errors.BadRequestError;
import com.example.courseprojectnetology.exception.errors.InternalServerError;
import com.example.courseprojectnetology.models.FilePlace;
import com.example.courseprojectnetology.models.NewFileName;
import com.example.courseprojectnetology.repository.FileRepository;
import com.example.courseprojectnetology.service.FileService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {


    String fileWay = "O:\\Личные файлы\\Netology\\fileBaseTest";
    @Autowired
    private FileRepository fileRepository;

    @Override
    @Transactional
    public ResponseEntity<String> uploadFileToServer(String name,
                                                     MultipartFile multipartFile) {

        String myltipatFileName = multipartFile.getOriginalFilename();

        String formatFile = getFileExtension(myltipatFileName);

        String fileWayOfSafeFile = fileWay + "\\" + name + formatFile;

        FilePlace filePlace = new FilePlace();
        filePlace.setFileName(name);
        filePlace.setFormatFile(formatFile);
        filePlace.setFileWayOf(fileWayOfSafeFile);
        int number = 1;
        try {
            if (multipartFile.isEmpty()) {
//*Ошибка загрузки файла
                throw new BadRequestError("Error input data", number);
            }
            Path test = Paths.get(fileWayOfSafeFile);

            Path destinationFile = test.resolve(
                            fileWayOfSafeFile)
                    .normalize().toAbsolutePath();

            if (Files.exists(test)) {
                return ResponseEntity.badRequest().body("Файл уже создан");
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
        }
        fileRepository.save(filePlace);
        return ResponseEntity.ok().build();
    }

    private static String getFileExtension(String mystr) {
        int index = mystr.indexOf('.');
        return index == -1 ? null : mystr.substring(index);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteFile(String name) {
        FilePlace filePlace = fileRepository.findByFileName(name);
        if (filePlace == null) {
            int number = 1;
            throw new BadRequestError("Error input data", number);
        }
        String fileWay = filePlace.getFileWayOf();
        try {
            Files.delete(Paths.get(fileWay));
        } catch (IOException e) {
            int number = 1;
            throw new InternalServerError("Error delete file", number);
        }
        long del = fileRepository.deleteFilePlaceByFileName(name);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<Resource> downloadFileFromCloud(String name) {
        FilePlace filePlace = fileRepository.findByFileName(name);
        if (filePlace == null) {
            int number = 1;
            throw new BadRequestError("Error input data", number);
        }

        try {
            Path file = Paths.get(filePlace.getFileWayOf());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + filePlace.getFileName() + filePlace.getFormatFile() + "\"")
                        .body(resource);
            } else {
                int number = 1;
                throw new InternalServerError("Error upload file", number);
            }
        } catch (MalformedURLException e) {
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    @Transactional
    public ResponseEntity<String> editFileName(String name, NewFileName newFileName) {
        FilePlace filePlace = fileRepository.findByFileName(name);
        if (filePlace == null) {
            int number = 1;
            throw new BadRequestError("Error input data", number);
        }
        Path source = Paths.get(filePlace.getFileWayOf());
        try {
            Files.move(source, source.resolveSibling(newFileName.getNewFileName() + filePlace.getFormatFile()));
        } catch (IOException e) {
            int number = 1;
            throw new InternalServerError("Error upload file", number);
        }
        filePlace.setFileName(newFileName.getNewFileName());
        filePlace.setFileWayOf(source.toString());
        fileRepository.save(filePlace);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<FilePlace>> getAllFiles() {
        List<FilePlace> filePlaces = fileRepository.findAll();
        if (filePlaces.isEmpty()) {
            int number = 1;
            throw new BadRequestError("Error input data", number);
        }
        //throw new InternetServerError("Error upload file", number);
        return ResponseEntity.ok()
                .body(filePlaces);
    }
}
