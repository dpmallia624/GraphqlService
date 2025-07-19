package com.sample.graphql.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SubjectRequest implements Serializable {

    private String subjectName;

    private Double marksObtained;
}
