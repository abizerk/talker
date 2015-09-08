package com.abizerk.talker.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.abizerk.talker.model.Fact;

public interface FactRepository extends MongoRepository<Fact, String>{
	
	public Fact findByFactValue(String factValue);
}
