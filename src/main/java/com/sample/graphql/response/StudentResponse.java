package com.sample.graphql.response;

import com.sample.graphql.entity.Student;
import com.sample.graphql.entity.Subject;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentResponse {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private Student student;
    //private List<SubjectResponse> subjectResponses;

    public StudentResponse (Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.email = student.getEmail();

        this.student = student;

//        if (student.getSubjects() != null) {
//            subjectResponses = new ArrayList<SubjectResponse>();
//            for (Subject subject: student.getSubjects()) {
//                subjectResponses.add(new SubjectResponse(subject));
//            }
//        }
    }

}
