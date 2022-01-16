package com.rbl.printworld.repositories;

import com.rbl.printworld.models.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends MongoRepository<Image, String> {

	@Query(value = "{'modelId' : ?0 }")
	Optional<List<Image>> findByModelId(String modelId);
}
