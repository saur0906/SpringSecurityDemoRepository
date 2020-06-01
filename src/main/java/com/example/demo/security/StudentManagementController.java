package com.example.demo.security;

import com.example.demo.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/management/api/v1/students")
public class StudentManagementController {


    private final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"Jamie Lanister"),
            new Student(2,"Cersie Lanister"),
            new Student(3,"Tyrione Lanister")
    );

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TRAINEEADMIN')")
    public List<Student> getAllStudents(){
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerStudent(@RequestBody Student student){
        System.out.println("method call registerStudent with parameter "+ student);
    }

    @PutMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable("studentId") Integer studentId,@RequestBody Student student){
        System.out.println("method call updateStudent with parameter "+ student +" id "+ studentId);
    }

    @DeleteMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId){
        System.out.println("method call delete Student with parameter "+ studentId);
    }

}
