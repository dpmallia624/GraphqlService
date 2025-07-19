package com.sample.graphql.configuration;

import com.sample.graphql.exception.StudentNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @GraphQlExceptionHandler(StudentNotFoundException.class)
    public GraphQLError handleStudentNotFound(StudentNotFoundException ex) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.NOT_FOUND)
                .message(ex.getMessage())
                .build();
    }

    @GraphQlExceptionHandler(IllegalArgumentException.class)
    public GraphQLError handleIllegalArgument(IllegalArgumentException e) {
        return GraphqlErrorBuilder.newError()
                .message(e.getMessage())
                .extensions(Collections.singletonMap("classification", "BAD_REQUEST"))
                .build();
    }

    @GraphQlExceptionHandler(Exception.class)
    public GraphQLError handleGeneralException(Exception e) {
        return GraphqlErrorBuilder.newError()
                .message("An unexpected error occurred")
                .extensions(Collections.singletonMap("classification", "INTERNAL_ERROR"))
                .build();
    }
}
