package com.example.courseprojectnetology.service.Impl;

import com.example.courseprojectnetology.dto.FileDTO;
import com.example.courseprojectnetology.exception.errors.BadRequestError;
import com.example.courseprojectnetology.exception.errors.InternalServerError;
import com.example.courseprojectnetology.models.FilePlace;
import com.example.courseprojectnetology.models.NewFileName;
import com.example.courseprojectnetology.repository.FileRepository;
import com.example.courseprojectnetology.service.FileService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Value("${my.property.startfileway}")
    private String fileWay;

    @Autowired
    private FileRepository fileRepository;

    @Override
    @Transactional
    public String uploadFileToServer(String name,
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
                //Добавить логирование
                throw new InternalServerError("Файл уже создан", number);
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ignored) {
        }
        fileRepository.save(filePlace);
        return "Success upload";
    }

    private static String getFileExtension(String mystr) {
        int index = mystr.indexOf('.');
        return index == -1 ? null : mystr.substring(index);
    }

    @Override
    @Transactional
    public String deleteFile(String name) {
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
        return "Success deleted";
    }

    @Override
    @Transactional
    public FileDTO downloadFileFromCloud(String name) {

        FilePlace filePlace = fileRepository.findByFileName(name);
        if (filePlace == null) {
            int number = 1;
            throw new BadRequestError("Error input data", number);
        }

        try {
            Path file = Paths.get(filePlace.getFileWayOf());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                FileDTO fileDTO = new FileDTO(String.valueOf(resource.hashCode()), resource.getContentAsByteArray().toString());
                return fileDTO;
            } else {
                int number = 1;
                throw new InternalServerError("Error upload file", number);
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    @Override
    @Transactional
    public String editFileName(String name, NewFileName newFileName) {
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
        return "Success upload";
    }

    @Override
    public List<FilePlace> getAllFiles() {
        List<FilePlace> filePlaces = fileRepository.findAll();
        if (filePlaces.isEmpty()) {
            int number = 1;
            throw new BadRequestError("Error input data", number);
        }
        for (FilePlace f : filePlaces) {
            if (Files.notExists(Paths.get(f.getFileWayOf()))) {
                int number = 1;
                throw new InternalServerError("Error upload file", number);
            }
        }
        return filePlaces;
    }
}
