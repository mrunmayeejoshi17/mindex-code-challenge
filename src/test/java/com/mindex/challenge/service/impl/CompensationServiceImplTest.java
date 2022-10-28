package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.assertj.core.util.DateUtil;
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

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.request.CompensationRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

	private String employeeCompensationUrl;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setup() {
		employeeCompensationUrl = "http://localhost:" + port + "/compensation/employee/";
	}

	@Test
	public void invalid_employeeid_should_return_exception_for_create() {
		CompensationRequest comp = new CompensationRequest();
		comp.setSalary(new BigDecimal("20000.99"));
		comp.setEffectiveDate(new Date());
		ResponseEntity<Compensation> createCompensationResponse = restTemplate
				.postForEntity(employeeCompensationUrl + "/id-does-not-exist", comp, Compensation.class);
		assertNotNull(createCompensationResponse);
		assertTrue(createCompensationResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Test
	public void should_return_exception_for_get_if_employee_exist_but_compensation_does_not() {
		ResponseEntity<Compensation> response = restTemplate
				.getForEntity(employeeCompensationUrl + "/c0c2293d-16bd-4603-8e08-638a9d18b22c", Compensation.class);
		assertNotNull(response);
		assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Test
	public void should_return_exception_for_field_validation_fail() {
		// invalid salary null
		CompensationRequest comp = new CompensationRequest();
		comp.setSalary(null);
		comp.setEffectiveDate(DateUtil.parse("2022-10-27"));
		ResponseEntity<Compensation> createCompensationResponse = restTemplate.postForEntity(
				employeeCompensationUrl + "/16a596ae-edd3-4847-99fe-c4518e82c86f", comp, Compensation.class);
		assertNotNull(createCompensationResponse);
		assertTrue(createCompensationResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

		// invalid salary less than 0
		comp = new CompensationRequest();
		comp.setSalary(new BigDecimal("-100"));
		comp.setEffectiveDate(DateUtil.parse("2022-10-27"));
		createCompensationResponse = restTemplate.postForEntity(
				employeeCompensationUrl + "/16a596ae-edd3-4847-99fe-c4518e82c86f", comp, Compensation.class);
		assertNotNull(createCompensationResponse);
		assertTrue(createCompensationResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

		// invalid salary is 0
		comp = new CompensationRequest();
		comp.setSalary(new BigDecimal("0"));
		comp.setEffectiveDate(DateUtil.parse("2022-10-27"));
		createCompensationResponse = restTemplate.postForEntity(
				employeeCompensationUrl + "/16a596ae-edd3-4847-99fe-c4518e82c86f", comp, Compensation.class);
		assertNotNull(createCompensationResponse);
		assertTrue(createCompensationResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

		// null date
		comp = new CompensationRequest();
		comp.setSalary(new BigDecimal("32442.99"));
		comp.setEffectiveDate(null);
		createCompensationResponse = restTemplate.postForEntity(
				employeeCompensationUrl + "/16a596ae-edd3-4847-99fe-c4518e82c86f", comp, Compensation.class);
		assertNotNull(createCompensationResponse);
		assertTrue(createCompensationResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Test
	public void test_should_create_and_get_employee_compensation() {
		// Initial get should return exception
		ResponseEntity<Compensation> response = restTemplate
				.getForEntity(employeeCompensationUrl + "/16a596ae-edd3-4847-99fe-c4518e82c86f", Compensation.class);
		assertNotNull(response);
		assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);

		CompensationRequest comp = new CompensationRequest();
		comp.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
		comp.setSalary(new BigDecimal("20000.99"));
		comp.setEffectiveDate(DateUtil.parse("2022-10-27"));

		// Creating Compensation successfully
		ResponseEntity<Compensation> createCompensationResponse = restTemplate.postForEntity(
				employeeCompensationUrl + "/16a596ae-edd3-4847-99fe-c4518e82c86f", comp, Compensation.class);
		assertNotNull(createCompensationResponse);
		assertTrue(createCompensationResponse.getStatusCode() == HttpStatus.OK);
		Compensation createCompensation = createCompensationResponse.getBody();
		assertNotNull(createCompensation);
		assertEquals(createCompensation.getEmployee().getEmployeeId(), "16a596ae-edd3-4847-99fe-c4518e82c86f");
		assertEquals(DateUtil.dayOfMonthOf(createCompensation.getEffectiveDate()), 27);
		assertEquals(DateUtil.monthOf(createCompensation.getEffectiveDate()), 10);
		assertTrue(createCompensation.getSalary().equals(new BigDecimal("20000.99")));

		// Get should return Compensation
		response = restTemplate.getForEntity(employeeCompensationUrl + "/16a596ae-edd3-4847-99fe-c4518e82c86f",
				Compensation.class);
		assertNotNull(response);
		assertTrue(response.getStatusCode() == HttpStatus.OK);
		Compensation compensation = response.getBody();
		assertNotNull(compensation);
		assertEquals(compensation.getEmployee().getEmployeeId(), "16a596ae-edd3-4847-99fe-c4518e82c86f");
		assertEquals(DateUtil.dayOfMonthOf(compensation.getEffectiveDate()), 27);
		assertEquals(DateUtil.monthOf(compensation.getEffectiveDate()), 10);
		assertTrue(compensation.getSalary().equals(new BigDecimal("20000.99")));
	}

}
