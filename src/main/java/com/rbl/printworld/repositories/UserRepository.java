package com.rbl.printworld.repositories;

import com.rbl.printworld.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.rbl.printWorld.config")
public interface UserRepository extends MongoRepository<User, String> {
	//
}
