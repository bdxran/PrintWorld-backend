package com.rbl.printworld.repositories;

import com.rbl.printworld.models.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends MongoRepository<Model, String> {

	@Query(value = "{'userId' : ?0 }")
	Optional<Page<Model>> findAllByUserByPage(String userId, PageRequest pageable);

	@Query(value = "{'userId' : ?0 }")
	Optional<List<Model>> findAllByUser(String userId);
}
