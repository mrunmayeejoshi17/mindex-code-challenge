package com.mindex.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@RestController
public class ReportingStructureController {
	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

	@Autowired
	private ReportingStructureService reportingStructureService;

	@GetMapping("/reporting-structure/employee/{id}")
	public ResponseEntity<ReportingStructure> read(@PathVariable String id) {
		LOG.debug("Received employee ID [{}] to display reporting structure.", id);
		ReportingStructure reportingStructure = reportingStructureService.getReportingStructure(id);
		if (reportingStructure == null) {
			return new ResponseEntity<ReportingStructure>(reportingStructure, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReportingStructure>(reportingStructure, HttpStatus.OK);
	}

}
