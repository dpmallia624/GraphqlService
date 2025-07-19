package com.sample.graphql.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddressRequest implements Serializable {

    private String street;

    private String city;
}
