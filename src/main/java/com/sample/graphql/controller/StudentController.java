package com.sample.graphql.controller;

import com.sample.graphql.entity.Student;
import com.sample.graphql.request.StudentRequest;
import com.sample.graphql.response.AddressResponse;
import com.sample.graphql.response.StudentResponse;
import com.sample.graphql.response.SubjectNameFilter;
import com.sample.graphql.response.SubjectResponse;
import com.sample.graphql.service.StudentService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @QueryMapping
    public StudentResponse studentById(@Argument Long id) {
        return new StudentResponse(studentService.getStudentById(id));
    }

    @SchemaMapping
    public List<SubjectResponse> subjectResponses(StudentResponse studentResponse,
                                                   @Argument("filter") SubjectNameFilter subjectNameFilter) {
        Student student = studentResponse.getStudent();
        if (student!= null && student.getSubjects() != null) {
            if (subjectNameFilter == null || "All".equalsIgnoreCase(subjectNameFilter.name())) {
                return student.getSubjects().stream()
                        .map(SubjectResponse::new)
                        .collect(Collectors.toList());
            }
            return student.getSubjects().stream()
                    .filter(subject -> subjectNameFilter.name().equalsIgnoreCase(subject.getSubjectName()))
                    .map(SubjectResponse::new).toList();
        }

        return Collections.emptyList();
    }

    @SchemaMapping
    public String fullName(StudentResponse studentResponse){
        return studentResponse.getFirstName()+", " + studentResponse.getLastName();
    }

    @SchemaMapping
    public AddressResponse addressResponse(StudentResponse studentResponse) {
        Student student = studentResponse.getStudent();
        if (student!= null && student.getAddress() != null) {
            return new AddressResponse(student.getAddress());
        }
        return null;
    }

    @MutationMapping
    public StudentResponse updateStudent(@Argument Long id, @Argument StudentRequest studentRequest){
        return new StudentResponse(studentService.updateStudent(id, studentRequest));
    }

    @MutationMapping
    public StudentResponse createStudent(@Argument StudentRequest studentRequest){
        return new StudentResponse(studentService.createStudent(studentRequest));
    }

    @MutationMapping
    public String deleteStudent(@Argument Long id){
        return studentService.deleteStudent(id);
    }
}
