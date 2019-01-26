package com.lopes.jpa_h2.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Employee {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	private String dept;

	private long salary;

	@ElementCollection
	private List<String> phoneNumbers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSalary() {
		return salary;
	}

	public void setSalary(long salary) {
		this.salary = salary;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public static Employee create(String name, String dept, long salary, String... phones) {
		Employee employee = new Employee();
		employee.setName(name);
		employee.setDept(dept);
		employee.setSalary(salary);
		if (phones != null) {
			employee.setPhoneNumbers(Arrays.asList(phones));
		}
		return employee;
	}

	@Override
	public String toString() {
		return "Employee{" + "id=" + id + ", name='" + name + '\'' + ", dept='" + dept + '\'' + ", salary=" + salary
				+ ", phoneNumbers=" + phoneNumbers + '}';
	}
}
