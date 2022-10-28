package com.mindex.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.request.CompensationRequest;
import com.mindex.challenge.service.CompensationService;
import com.mongodb.lang.NonNull;

@RestController
public class CompensationController {
	private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

	@Autowired
	private CompensationService compensationService;

	@PostMapping("/compensation/employee/{id}")
	public ResponseEntity<Compensation> create(@PathVariable String id,
			@RequestBody CompensationRequest compensationRequest) {
		LOG.debug("Received compensation request to create [{}] for employee Id [{}]", compensationRequest, id);
		Compensation compensation = compensationService.createCompensation(id, compensationRequest);
		return sendResponse(compensation);
	}

	@GetMapping("/compensation/employee/{id}")
	public ResponseEntity<Compensation> read(@NonNull @PathVariable String id) {
		LOG.debug("Received employee id [{}] to get the Compensation details.", id);
		Compensation compensation = compensationService.getCompensation(id);
		return sendResponse(compensation);
	}

	private ResponseEntity<Compensation> sendResponse(Compensation compensation) {
		if (compensation == null) {
			return new ResponseEntity<Compensation>(compensation, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Compensation>(compensation, HttpStatus.OK);
	}
}
