package com.example.courseprojectnetology.repository;

import com.example.courseprojectnetology.models.FilePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FilePlace, Long> {
    FilePlace findByFileName(String fileName);
    long deleteFilePlaceByFileName(String fileName);
}
