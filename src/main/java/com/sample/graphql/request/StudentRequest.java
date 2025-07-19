package com.sample.graphql.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

//@Builder
@Getter
@Setter
public class StudentRequest implements Serializable {

    private String firstName;

    private String lastName;

    private String email;

    private List<SubjectRequest> subjectRequests;

    private AddressRequest addressRequest;
}
