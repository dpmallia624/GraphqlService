package com.sample.graphql;

import com.sample.graphql.configuration.GlobalExceptionHandler;
import com.sample.graphql.controller.StudentController;
import com.sample.graphql.entity.Address;
import com.sample.graphql.entity.Student;
import com.sample.graphql.exception.StudentNotFoundException;
import com.sample.graphql.request.StudentRequest;
import com.sample.graphql.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@GraphQlTest(StudentController.class)
@Import(GlobalExceptionHandler.class)
public class StudentControllerTests {
    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    StudentService studentServiceMock;

    @Test
    void shouldReturnStudent() {

        Student student = new Student();
        student.setId(1L);
        student.setEmail("roahn.m@gmail.com");
        student.setFirstName("Rohan");
        student.setLastName("M");
        Address address = new Address();
        address.setId(7L);
        address.setStreet("5th cross main");
        address.setCity("bangalore");
        student.setAddress(address);

        Mockito.when(studentServiceMock.getStudentById(anyLong())).thenReturn(student);

        this.graphQlTester
                .documentName("student")
                .variable("id", 1)
                .execute()
                .path("studentById")
                .matchesJson("""                        
                     {
                         "id": "1",
                         "email": "roahn.m@gmail.com",
                         "firstName": "Rohan",
                         "lastName": "M",
                         "fullName": "Rohan, M"
                     },
                     "addressResponse": {
                         "id": "7",
                         "street": "5th cross main",
                         "city": "bangalore"
                     }
                """);
    }

    @Test
    void shouldReturnErrorQueryStudent() {
        Mockito.when(studentServiceMock.getStudentById(anyLong())).thenThrow(new StudentNotFoundException("Student with given ID not exists"));

        this.graphQlTester
                .documentName("student")
                .variable("id", 1)
                .execute()
                .errors()
                .satisfy(
                        responseErrors -> {
                            assertThat(responseErrors).hasSize(1);
                            assertThat(responseErrors.get(0).getErrorType().toString()).isEqualTo("NOT_FOUND");
                        });
    }

    @Test
    void canCreateStudent() {

        Map<String, Object> studentRequestMap = Map.of(
                "firstName", "First",
                "lastName", "last",
                "email", "myuser@test.com"
        );

        Student student = new Student();
        student.setId(1L);
        student.setEmail("roahn.m@gmail.com");
        student.setFirstName("Rohan");
        student.setLastName("M");
        Address address = new Address();
        address.setId(7L);
        address.setStreet("5th cross main");
        address.setCity("bangalore");
        student.setAddress(address);

        Mockito.when(studentServiceMock.createStudent(any(StudentRequest.class))).thenReturn(student);

        this.graphQlTester.documentName("createStudent")
                .variable("studentRequest", studentRequestMap)
                .execute()
                .path("createStudent")
                .matchesJson("""                        
                     {
                         "id": "1",
                         "email": "roahn.m@gmail.com",
                         "firstName": "Rohan",
                         "lastName": "M"
                     },
                     "addressResponse": {
                         "id": "7",
                         "street": "5th cross main",
                         "city": "bangalore"
                     }
                """);
    }

    @Test
    void canNotCreateStudent() {

        Map<String, Object> studentRequestMap = Map.of(
                "firstName", "First",
                "lastName", "last",
                "email", "myuser@test.com"
        );

        Mockito.when(studentServiceMock.createStudent(any(StudentRequest.class)))
                .thenThrow(new StudentNotFoundException("Student with given ID not exists"));

        this.graphQlTester.documentName("createStudent")
                .variable("studentRequest", studentRequestMap)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getErrorType().toString()).isEqualTo("NOT_FOUND");
                });
    }

    @Test
    void canUpdateStudent() {

        Map<String, Object> studentRequestMap = Map.of(
                "firstName", "First",
                "lastName", "last",
                "email", "myuser@test.com"
        );

        Student student = new Student();
        student.setId(1L);
        student.setEmail("roahn.m@gmail.com");
        student.setFirstName("Rohan");
        student.setLastName("M");
        Address address = new Address();
        address.setId(7L);
        address.setStreet("5th cross main");
        address.setCity("bangalore");
        student.setAddress(address);

        Mockito.when(studentServiceMock.updateStudent(any(Long.class), any(StudentRequest.class))).thenReturn(student);

        this.graphQlTester.documentName("studentUpdate")
                .variable("id", 1L)
                .variable("studentRequest", studentRequestMap)
                .execute()
                .path("updateStudent")
                .matchesJson("""                        
                     {
                         "id": "1",
                         "email": "roahn.m@gmail.com",
                         "firstName": "Rohan",
                         "lastName": "M"
                     },
                     "addressResponse": {
                         "id": "7",
                         "street": "5th cross main",
                         "city": "bangalore"
                     }
                """);
    }

    @Test
    void canNotUpdateStudent() {

        Map<String, Object> studentRequestMap = Map.of(
                "firstName", "First",
                "lastName", "last",
                "email", "myuser@test.com"
        );

        Mockito.when(studentServiceMock.updateStudent(any(Long.class), any(StudentRequest.class)))
                .thenThrow(new StudentNotFoundException("Student with given ID not exists"));

        this.graphQlTester.documentName("studentUpdate")
                .variable("id", 1L)
                .variable("studentRequest", studentRequestMap)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getErrorType().toString()).isEqualTo("NOT_FOUND");
                });
    }

    @Test
    void canDeleteStudent() {

        String expectedResult = "Student deleted with id 9";

        Mockito.when(studentServiceMock.deleteStudent(any(Long.class))).thenReturn(expectedResult);

        this.graphQlTester.documentName("deleteStudent")
                .variable("id", 9L)
                .execute()
                .path("deleteStudent")
                .entity(String.class)
                .isEqualTo(expectedResult);
    }

    @Test
    void canNotDeleteStudent() {

        Mockito.when(studentServiceMock.deleteStudent(any(Long.class)))
                .thenThrow(new StudentNotFoundException("Student with given ID not exists"));

        this.graphQlTester.documentName("deleteStudent")
                .variable("id", 9L)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getErrorType().toString()).isEqualTo("NOT_FOUND");
                });
    }
}
