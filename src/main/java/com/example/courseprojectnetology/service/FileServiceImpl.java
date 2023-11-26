package com.example.courseprojectnetology.service;

import com.example.courseprojectnetology.models.FilePlace;
import com.example.courseprojectnetology.repository.FileRepository;
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
//@RequiredArgsConstructor
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

        try {
            if (multipartFile.isEmpty()) {
                //throw new StorageException("Failed to store empty file.");
            }
            Path test = Paths.get(fileWayOfSafeFile);

            Path destinationFile = test.resolve(
                            fileWayOfSafeFile)
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(test.toAbsolutePath())) {
                // This is a security check
//                throw new StorageException(
//                        "Cannot store file outside current directory.");

            }
            if (Files.exists(test)) {
                return ResponseEntity.badRequest().body("Файл уже создан");
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            //throw new StorageException("Failed to store file.", e);
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
        String fileWay = filePlace.getFileWayOf();
        try {
            Files.delete(Paths.get(fileWay));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        long del = fileRepository.deleteFilePlaceByFileName(name);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<Resource> downloadFileFromCloud(String name) {
        FilePlace filePlace = fileRepository.findByFileName(name);
//        public ResponseEntity<byte[]> getFile(@PathVariable String id) {
//            FileDB fileDB = storageService.getFile(id);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
//                    .body(fileDB.getData());
//        }
//        try {
//
//            byte[] file = Files.readAllBytes(Paths.get(filePlace.getFileWayOf()));
//            return ResponseEntity.ok().headers()
//                    .body(file);
//        } catch (IOException e){
//
//        }


        try {
            //Path file = root.resolve(name);
            Path file = Paths.get(filePlace.getFileWayOf());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + filePlace.getFileName() + filePlace.getFormatFile() + "\"")
                        .body(resource);
            } else {
                //throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            //throw new RuntimeException("Error: " + e.getMessage());
        }

        return ResponseEntity.badRequest().build();
    }

    @Override
    @Transactional
    public ResponseEntity<String> editFileName(String name, String newFileName) {
        FilePlace filePlace = fileRepository.findByFileName(name);
        Path source = Paths.get(filePlace.getFileWayOf());

        try {
            Files.move(source, source.resolveSibling(name + filePlace.getFormatFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        filePlace.setFileName(name);
        filePlace.setFileWayOf(source.toString());
        fileRepository.save(filePlace);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<FilePlace>> getAllFiles() {
        List<FilePlace> filePlaces = fileRepository.findAll();
        return ResponseEntity.ok()
                .body(filePlaces);
    }
}
