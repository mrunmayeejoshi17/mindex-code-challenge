package com.mindex.challenge.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public ReportingStructure getReportingStructure(String employeeId) {
		LOG.debug("Getting reporting structure for employee id: [{}]", employeeId);
		List<Employee> employees = employeeRepository.findAll();
		if (CollectionUtils.isEmpty(employees)) {
			LOG.error("No employee data");
			return null;
		}
		Map<String, Employee> employeeByIdMap = employees.stream()
				.collect(Collectors.toMap(Employee::getEmployeeId, Function.identity()));

		if (!employeeByIdMap.containsKey(employeeId)) {
			LOG.error("Invalid employee id :" + employeeId);
			return null;
		}
		Employee employee = employeeByIdMap.get(employeeId);
		Map<String, List<Employee>> managerReporteesMap = employees.stream()
				.filter(e -> !CollectionUtils.isEmpty(e.getDirectReports()))
				.collect(Collectors.toMap(Employee::getEmployeeId, Employee::getDirectReports));
		int reporteeCount = getReporteeCount(employeeId, managerReporteesMap);
		LOG.debug("Total report count for employee [{}] is [{}]", employeeId, reporteeCount);
		ReportingStructure rportingStructure = new ReportingStructure();
		rportingStructure.setEmployee(employee);
		rportingStructure.setNumberOfReports(reporteeCount);
		return rportingStructure;
	}

	private int getReporteeCount(String employeeId, Map<String, List<Employee>> managerReporteesMap) {
		int count = 0;
		if (!managerReporteesMap.containsKey(employeeId)) {
			return 0;
		}
		List<Employee> directReportee = managerReporteesMap.get(employeeId);
		count = directReportee.size();

		for (Employee emp : directReportee) {
			count += getReporteeCount(emp.getEmployeeId(), managerReporteesMap);
		}
		return count;
	}

}
