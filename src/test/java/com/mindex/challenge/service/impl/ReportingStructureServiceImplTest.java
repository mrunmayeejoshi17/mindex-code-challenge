package com.mindex.challenge.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		employeeReportingCountMap.put("16a596ae-edd3-4847-99fe-c4518e82c86f", 4);
		employeeReportingCountMap.put("b7839309-3348-463b-a7e3-5de1c168beb3", 0);
		employeeReportingCountMap.put("03aa1462-ffa9-4978-901b-7c001562cf6f", 2);
		employeeReportingCountMap.put("62c1084e-6e34-4630-93fd-9153afb65309", 0);
		employeeReportingCountMap.put("c0c2293d-16bd-4603-8e08-638a9d18b22c", 0);
	}

	@Test
	public void should_return_exception_for_get_employee_reporting_structure_if_employee_not_exist() {
		ResponseEntity<ReportingStructure> response = restTemplate
				.getForEntity(reportingStructureUrl + "/employee-id-does-not-exist", ReportingStructure.class);
		assertNotNull(response);
		assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Test
	public void test_should_get_employee_reporting_structure() {
		for (String employeeId : employeeReportingCountMap.keySet()) {
			int expectedNumOfReports = employeeReportingCountMap.get(employeeId);
			ResponseEntity<ReportingStructure> createReportingStructureResponse = restTemplate
					.getForEntity(reportingStructureUrl + "/" + employeeId, ReportingStructure.class);
			assertNotNull(createReportingStructureResponse);
			assertTrue(createReportingStructureResponse.getStatusCode() == HttpStatus.OK);
			ReportingStructure reportingStructure = createReportingStructureResponse.getBody();
			assertNotNull(reportingStructure);
			assertEquals(reportingStructure.getEmployee().getEmployeeId(), employeeId);
			assertEquals(reportingStructure.getNumberOfReports(), expectedNumOfReports);
		}
	}

}
