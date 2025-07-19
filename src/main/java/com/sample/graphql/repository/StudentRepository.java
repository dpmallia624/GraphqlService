package com.sample.graphql.repository;

import com.sample.graphql.entity.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
