package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.ReportingStructure;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private static Map<String, Integer> employeeReportingCountMap = new HashMap<>();
	private String reportingStructureUrl;

	@Before
	public void setup() {
		reportingStructureUrl = "http://localhost:" + port + "/reporting-structure/employee/";

	}

	@Test
	public void should_return_exception_for_get_employee_reporting_structure_if_employee_not_exist() {
		reportingStructureUrl = "http://localhost:" + port + "/reporting-structure/employee/";
		ResponseEntity<ReportingStructure> response = restTemplate
				.getForEntity(reportingStructureUrl + "employee-id-does-not-exist", ReportingStructure.class);
		assertNotNull(response);
		assertTrue(response.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR));

	}
	
	public void test_should_get_employee_reporting_structure() {
		String employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
		int expectedNumOfReports = 4;
		reportingStructureUrl = "http://localhost:" + port + "/reporting-structure/employee/";
		ResponseEntity<ReportingStructure> createReportingStructureResponse = restTemplate
				.getForEntity(reportingStructureUrl + employeeId, ReportingStructure.class);
		assertNotNull(createReportingStructureResponse);
		ReportingStructure reportingStructure = createReportingStructureResponse.getBody();
		assertNotNull(reportingStructure);
		 assertNotNull(reportingStructure.getEmployee());
		 assertEquals(employeeId, reportingStructure.getEmployee().getEmployeeId());
		 assertNotNull(reportingStructure.getNumberOfReports());
		assertEquals(reportingStructure.getNumberOfReports(), expectedNumOfReports);
	}

}
