package com.abizerk.talker.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.abizerk.talker.model.Fact;
import com.abizerk.talker.model.Link;

public interface LinkRepository extends MongoRepository<Link, String>{
	
	public List<Link> findByParentFact_factValue(String parentFactValue);
	
	public List<Link> findByChildFact_factValue(String childFactValue);
	
	public List<Link> findByParentFactFactValueAndChildFactFactValue(String parentFactValue, String childFactValue);

}
