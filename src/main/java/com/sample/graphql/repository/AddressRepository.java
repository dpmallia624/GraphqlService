package com.sample.graphql.repository;

import com.sample.graphql.entity.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
