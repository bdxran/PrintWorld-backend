package com.rbl.printworld.repositories;

import com.rbl.printworld.models.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<Image, String> {
}
