package com.mindex.challenge.data;

import java.math.BigDecimal;
import java.util.Date;

import com.mindex.challenge.request.CompensationRequest;

public class Compensation {

	private Employee employee;
	private BigDecimal salary;
	private Date effectiveDate;

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
