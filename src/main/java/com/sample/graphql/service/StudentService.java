package com.sample.graphql.service;

import com.sample.graphql.entity.Student;
import com.sample.graphql.request.StudentRequest;

public interface StudentService {
    Student getStudentById(long id);

    Student createStudent(StudentRequest studentRequest);

    Student updateStudent(Long id, StudentRequest studentRequest);

    String deleteStudent(Long id);
}
