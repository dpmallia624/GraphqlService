package com.sample.graphql.repository;

import com.sample.graphql.entity.Subject;
import org.springframework.data.repository.CrudRepository;

public interface SubjectRepository extends CrudRepository<Subject, Long> {
}
