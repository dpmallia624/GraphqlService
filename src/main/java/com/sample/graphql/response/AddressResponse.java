package com.sample.graphql.response;

import com.sample.graphql.entity.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {

    private Long id;

    private String street;

    private String city;

    public AddressResponse(Address address) {
      this.id = address.getId();
      this.city = address.getCity();
      this.street = address.getStreet();
    }
}
