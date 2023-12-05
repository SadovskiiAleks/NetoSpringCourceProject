package com.example.courseprojectnetology.service.Impl;

import com.example.courseprojectnetology.dto.FileDTO;
import com.example.courseprojectnetology.exception.errors.BadRequestError;
import com.example.courseprojectnetology.exception.errors.InternalServerError;
import com.example.courseprojectnetology.models.FilePlace;
import com.example.courseprojectnetology.models.NewFileName;
import com.example.courseprojectnetology.repository.FileRepository;
import com.example.courseprojectnetology.service.FileService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${my.property.startfileway}")
    private String fileWay;

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

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
        try {
            if (multipartFile.isEmpty()) {
            //*Ошибка загрузки файла
                throw new BadRequestError("Error input data", ExceptionSingletonServiceImpl.getInstance().getId());
            }
            Path nameOfFileWay = Paths.get(fileWayOfSafeFile);

            Path destinationFile = nameOfFileWay.resolve(
                            fileWayOfSafeFile)
                    .normalize().toAbsolutePath();

            if (Files.exists(nameOfFileWay)) {
                //Добавить логирование
                throw new InternalServerError("File exist ", ExceptionSingletonServiceImpl.getInstance().getId());
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ignored) {
        }
        fileRepository.save(filePlace);
        log.info("Success upload: " + name);
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
            throw new BadRequestError("Error input data", ExceptionSingletonServiceImpl.getInstance().getId());
        }
        String fileWay = filePlace.getFileWayOf();
        try {
            Files.delete(Paths.get(fileWay));
        } catch (IOException e) {
            throw new InternalServerError("Error delete file", ExceptionSingletonServiceImpl.getInstance().getId());
        }
        long del = fileRepository.deleteFilePlaceByFileName(name);
        log.info("Success deleted:" + name);
        return "Success deleted";
    }

    @Override
    @Transactional
    public FileDTO downloadFileFromCloud(String name) {
        FilePlace filePlace = fileRepository.findByFileName(name);
        if (filePlace == null) {
            throw new BadRequestError("Error input data", ExceptionSingletonServiceImpl.getInstance().getId());
        }

        try {
            Path file = Paths.get(filePlace.getFileWayOf());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                FileDTO fileDTO = new FileDTO(String.valueOf(resource.hashCode()), resource.getContentAsByteArray().toString());
                log.info("Success send:" + name);
                return fileDTO;
            } else {
                throw new InternalServerError("Error upload file", ExceptionSingletonServiceImpl.getInstance().getId());
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
            throw new BadRequestError("Error input data", ExceptionSingletonServiceImpl.getInstance().getId());
        }
        Path source = Paths.get(filePlace.getFileWayOf());
        try {
            Files.move(source, source.resolveSibling(newFileName.getNewFileName() + filePlace.getFormatFile()));

        } catch (IOException e) {
            throw new InternalServerError("Error upload file", ExceptionSingletonServiceImpl.getInstance().getId());
        }
        filePlace.setFileName(newFileName.getNewFileName());
        filePlace.setFileWayOf(source.toString());
        fileRepository.save(filePlace);
        log.info("Success edit file name. New file name is:" + name);
        return "Success upload";
    }

    @Override
    public List<FilePlace> getAllFiles() {
        List<FilePlace> filePlaces = fileRepository.findAll();
        if (filePlaces.isEmpty()) {
            throw new BadRequestError("Error input data", ExceptionSingletonServiceImpl.getInstance().getId());
        }
        for (FilePlace f : filePlaces) {
            if (Files.notExists(Paths.get(f.getFileWayOf()))) {
                throw new InternalServerError("Error upload file", ExceptionSingletonServiceImpl.getInstance().getId());
            }
        }
        log.info("Success set file list");
        return filePlaces;
    }
}
