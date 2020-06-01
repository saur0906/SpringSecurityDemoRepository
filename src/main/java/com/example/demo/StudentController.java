package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"Jamie Lanister"),
            new Student(2,"Cersie Lanister"),
            new Student(3,"Tyrione Lanister")
    );

    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId){
        return STUDENTS.stream().filter(student -> studentId.equals(student.getStudentId())).
                findFirst().orElseThrow( () -> new IllegalStateException("Student not found with id "+studentId) );
    }
}
