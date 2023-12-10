package com.example.courseprojectnetology;

import com.example.courseprojectnetology.models.FilePlace;
import com.example.courseprojectnetology.models.NewFileName;
import com.example.courseprojectnetology.repository.FileRepository;
import com.example.courseprojectnetology.service.Impl.FileServiceImpl;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CourseProjectNetologyApplicationTests {
    private static FileServiceImpl fileService;
    private static FileRepository fileRepository;

    private static String fileWayOfTestFileOne = "O:\\Личные файлы\\Netology\\test\\testFile.jpg";
    private static String fileNameFileOne = "testFile";

    private static String fileWayOfTestFileTwo = "O:\\Личные файлы\\Netology\\fileBaseTest\\testFile.jpg";
    private static String fileNameFileTwo = "testFile";
    private static String formatFile = ".jpg";
    private static FilePlace filePlaceFileTwo = new FilePlace();

    private static String fileWayOfTestFileThree = "O:\\Личные файлы\\Netology\\fileBaseTest\\testFile2.jpg";
    private static String fileNameFileThree = "testFile2";
    private static NewFileName newFileNameFileThree = new NewFileName();
    private static FilePlace filePlaceFileThree = new FilePlace();

    @BeforeAll
    public static void get() {
        fileRepository = Mockito.mock(FileRepository.class);
        fileService = new FileServiceImpl(fileRepository);
        ReflectionTestUtils.setField(fileService, "fileWay", "O:\\Личные файлы\\Netology\\fileBaseTest");

        filePlaceFileTwo.setFileName(fileNameFileTwo);
        filePlaceFileTwo.setFormatFile(formatFile);
        filePlaceFileTwo.setFileWayOf(fileWayOfTestFileTwo);

        filePlaceFileThree.setFileName(fileNameFileThree);
        filePlaceFileThree.setFormatFile(formatFile);
        filePlaceFileThree.setFileWayOf(fileWayOfTestFileThree);

        newFileNameFileThree.setNewFileName(fileNameFileThree);
    }

    @Test
    public void upload() {
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile(fileNameFileOne, fileWayOfTestFileOne, " ", Files.readAllBytes(Paths.get(fileWayOfTestFileOne)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileService.uploadFileToServer(fileNameFileOne, multipartFile);
        assertTrue(Files.exists(Paths.get(fileWayOfTestFileTwo)));
    }

    @Test
    public void editFile() {
        Mockito.when(fileRepository.findByFileName(fileNameFileTwo)).thenReturn(filePlaceFileTwo);
        fileService.editFileName(fileNameFileTwo, newFileNameFileThree);
        assertTrue(Files.exists(Paths.get(fileWayOfTestFileThree)));
    }


    @Test
    public void getAllFiles() {
        List<FilePlace> filePlaces = new ArrayList<>();
        filePlaces.add(filePlaceFileTwo);
        filePlaces.add(filePlaceFileThree);
        Mockito.when(fileRepository.findAll()).thenReturn(filePlaces);
        List<FilePlace> test = fileService.getAllFiles();
        assertTrue(test.size() == 2);
    }


    @Test
    public void deleteFileByName() {
        Mockito.when(fileRepository.findByFileName(fileNameFileThree)).thenReturn(filePlaceFileThree);
        fileService.deleteFile(fileNameFileThree);
        assertTrue(Files.notExists(Paths.get(fileWayOfTestFileThree)));
    }
}
