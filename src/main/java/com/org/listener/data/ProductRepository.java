package com.org.listener.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
	
	List<Product> findByNameIn(List<String> name);

//	List<Product> findByNameInAndLocation(List<String> name);

}