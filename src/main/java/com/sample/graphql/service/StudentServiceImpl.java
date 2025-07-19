package com.sample.graphql.service;

import com.sample.graphql.entity.Address;
import com.sample.graphql.entity.Student;
import com.sample.graphql.entity.Subject;
import com.sample.graphql.exception.StudentNotFoundException;
import com.sample.graphql.repository.StudentRepository;
import com.sample.graphql.request.AddressRequest;
import com.sample.graphql.request.StudentRequest;
import com.sample.graphql.request.SubjectRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getStudentById(long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with given ID not exists"));
    }

    @Override
    @Transactional
    public Student createStudent(StudentRequest studentRequest) {
        Student student = new Student();
        student.setFirstName(studentRequest.getFirstName());
        student.setLastName(studentRequest.getLastName());
        student.setEmail(studentRequest.getEmail());
        AddressRequest addressRequest = studentRequest.getAddressRequest();
        if(addressRequest != null) {
            Address address = new Address();
            address.setCity(addressRequest.getCity());
            address.setStreet(addressRequest.getStreet());
            student.setAddress(address);
        }

        List<SubjectRequest> subjectRequests = studentRequest.getSubjectRequests();
        if(!CollectionUtils.isEmpty(subjectRequests)){
            student.getSubjects().addAll(
                    subjectRequests.stream()
                            .map(subjectRequest -> {
                                Subject subject = new Subject();
                                subject.setSubjectName(subjectRequest.getSubjectName());
                                subject.setMarksObtained(subjectRequest.getMarksObtained());
                                subject.setStudent(student);
                                return subject;
                            }).toList());
        }

        studentRepository.save(student);

        return student;
    }

    @Override
    public Student updateStudent(Long id, StudentRequest studentRequest) {

        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with given ID not exists"));

        student.setFirstName(studentRequest.getFirstName());
        student.setLastName(studentRequest.getLastName());
        student.setEmail(studentRequest.getEmail());
        AddressRequest addressRequest = studentRequest.getAddressRequest();
        if(addressRequest != null) {
            Address address = student.getAddress();
            address.setCity(addressRequest.getCity());
            address.setStreet(addressRequest.getStreet());
            student.setAddress(address);
        }

        List<SubjectRequest> subjectRequests = studentRequest.getSubjectRequests();
        if(!CollectionUtils.isEmpty(subjectRequests)){
            student.getSubjects().addAll(
                    subjectRequests.stream()
                            .map(subjectRequest -> {
                                Subject subject = findSubject(student, subjectRequest);
                                if(subject == null){
                                    subject = new Subject();
                                    subject.setSubjectName(subjectRequest.getSubjectName());
                                    subject.setStudent(student);
                                }
                                subject.setMarksObtained(subjectRequest.getMarksObtained());

                                return subject;
                            }).toList());
        }

        studentRepository.save(student);
        return student;
    }

    @Override
    public String deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with given ID not exists"));

        studentRepository.delete(student);
        return "Student deleted with id "+ id;
    }

    private Subject findSubject(Student student, SubjectRequest subjectRequest){
        return student.getSubjects()
                .stream()
                .filter(studentSubject -> studentSubject.getSubjectName().equalsIgnoreCase(subjectRequest.getSubjectName()))
                .findFirst()
                .orElse(null);
    }

}
