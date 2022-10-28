package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.request.CompensationRequest;

public interface CompensationService {
	Compensation createCompensation(String employeeId, CompensationRequest compensationRequest);
    Compensation getCompensation(String employeeId);
}
