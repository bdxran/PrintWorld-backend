package com.rbl.printworld.repositories;

import com.rbl.printworld.models.Model;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends MongoRepository<Model, String> {
}
