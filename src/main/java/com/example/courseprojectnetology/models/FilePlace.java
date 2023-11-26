package com.example.courseprojectnetology.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class FilePlace {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String formatFile;

    private String fileWayOf;
}
