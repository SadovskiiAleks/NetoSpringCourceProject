package com.example.courseprojectnetology;

import com.example.courseprojectnetology.controller.Impl.FileLoadControllerImp;
import com.example.courseprojectnetology.controller.Impl.LoginController;
import com.example.courseprojectnetology.dto.FileDTO;
import com.example.courseprojectnetology.models.FilePlace;
import com.example.courseprojectnetology.models.NewFileName;
import com.example.courseprojectnetology.repository.FileRepository;
import com.example.courseprojectnetology.security.jwtMy.MyUserDetailsService;
import com.example.courseprojectnetology.security.jwtMy.token.JWTTokenProvider;
import com.example.courseprojectnetology.service.Impl.FileServiceImpl;
import com.example.courseprojectnetology.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@ContextConfiguration
//@TestPropertySource(properties = { "fileWay = O:/?????? ?????/Netology/fileBaseTest"})
class CourseProjectNetologyApplicationTests {
    private static FileServiceImpl fileService;
    private static FileRepository fileRepository;

    @BeforeAll
    public static void getFileService() {
         fileRepository =Mockito.spy(FileRepository.class);
       fileService = new FileServiceImpl(fileRepository);
       ReflectionTestUtils.setField(fileService, "fileWay", "O:\\Личные файлы\\Netology\\fileBaseTest");

    }

//    @Test
//    public void uploadFileToServer() {
//        String fileWayOfTestFile = "O:\\Личные файлы\\Netology\\test\\testFile.jpg";
//        String name = "testFile";
//        MultipartFile multipartFile = null;
//        try {
//            multipartFile = new MockMultipartFile(name,fileWayOfTestFile," ", Files.readAllBytes(Paths.get(fileWayOfTestFile)));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        fileService.uploadFileToServer(name,multipartFile);
//
//        String fileWayOfUploadFile = "O:\\Личные файлы\\Netology\\fileBaseTest\\testFile.jpg";
//        assertTrue(Files.exists(Paths.get(fileWayOfUploadFile)));
//    }

//    @Test
//    public void editFileName() {
//        String name = "testFile";
//        NewFileName newFileName = new NewFileName();
//        newFileName.setNewFileName("testFile2.jpg");
//        FilePlace filePlace = new FilePlace();
//        filePlace.setFileName("testFile");
//        filePlace.setFormatFile(".jpg");
//        filePlace.setFileWayOf("O:\\Личные файлы\\Netology\\fileBaseTest\\testFile.jpg");
//        //Mockito.when(fileRepository.findByFileName("")).thenReturn(filePlace);
//        Mockito.doReturn(filePlace).when(fileRepository).findByFileName("");
//
//        fileService.editFileName(name, newFileName);
//
//        String fileWayOfUploadFile = "O:\\Личные файлы\\Netology\\fileBaseTest\\testFile2.jpg";
//        assertTrue(Files.exists(Paths.get(fileWayOfUploadFile)));
//    }



    @Test
    public void getAllFiles() {
        String fileName = "testFile2.jpg";
        List<FilePlace>  test = fileService.getAllFiles();
        //
        assertTrue(test != null);
    }

//
//    @Test
//    public void deleteFile() {
//        //отправить запрос на удаление файла
//
//        String fileWayOfUploadFile = "O:\\Личные файлы\\Netology\\fileBaseTest\\testFile2.jpg";
//        assertTrue(Files.notExists(Paths.get(fileWayOfUploadFile)));
//    }

    ;
}
