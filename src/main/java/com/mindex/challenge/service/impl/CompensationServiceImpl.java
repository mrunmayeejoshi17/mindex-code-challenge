package com.mindex.challenge.service.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.request.CompensationRequest;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {
	private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

	@Autowired
	private CompensationRepository compensationRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Compensation createCompensation(String employeeId, CompensationRequest compensationRequest) {
		LOG.debug("Creating compensation [{}] for employee Id: [{}]", compensationRequest, employeeId);
		Employee employee = validateEmployeeById(employeeId);
		if (employee == null || compensationRequest == null || compensationRequest.getEffectiveDate() == null
				|| compensationRequest.getSalary() == null
				|| compensationRequest.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
			return null;
		}
		compensationRequest.setEmployeeId(employeeId);
		compensationRepository.insert(compensationRequest);
		return buildCompensation(employee, compensationRequest);
	}

	@Override
	public Compensation getCompensation(String employeeId) {
		LOG.debug("Getting compensation for employee [{}]", employeeId);

		Employee employee = validateEmployeeById(employeeId);
		if (employee == null) {
			return null;
		}
		CompensationRequest cr = compensationRepository.findCompensationByEmployeeId(employeeId);
		if (cr == null) {
			return null;
		}
		Compensation comp = buildCompensation(employee, cr);
		return comp;
	}

	private Compensation buildCompensation(Employee employee, CompensationRequest cr) {
		Compensation comp = new Compensation();
		comp.setEmployee(employee);
		comp.setEffectiveDate(cr.getEffectiveDate());
		comp.setSalary(cr.getSalary());
		return comp;
	}

	private Employee validateEmployeeById(String employeeId) {
		if (employeeId == null || "".equals(employeeId.trim())) {
			LOG.error("employeeId can not be null or blank.");
			return null;
		}
		Employee employee = employeeRepository.findByEmployeeId(employeeId);
		if (employee == null) {
			LOG.error("Invalid employeeId: " + employeeId);
			return null;
		}
		return employee;
	}

}
