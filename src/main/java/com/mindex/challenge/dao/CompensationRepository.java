package com.mindex.challenge.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mindex.challenge.request.CompensationRequest;

@Repository
public interface CompensationRepository extends MongoRepository<CompensationRequest, String> {
	CompensationRequest findCompensationByEmployeeId(String employeeId);
}
