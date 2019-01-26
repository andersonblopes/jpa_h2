package com.lopes.jpa_h2;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.lopes.jpa_h2.model.Employee;

public class Principal {

	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

	public static void main(String[] args) {
		try {
			persistEmployees();
			findEmployeeByPhoneCount();
			findDeptHavingAboveNetAverage();
			findEmployeeWithLessThanAverageSalary();
		} finally {
			entityManagerFactory.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static void persistEmployees() {
		Employee employee1 = Employee.create("Diana", "IT", 3000, "111-111-111", "222-111-111");
		Employee employee2 = Employee.create("Rose", "Admin", 2000, "333-111-111", "444-111-111");
		Employee employee3 = Employee.create("Denise", "Admin", 4000, "555-111-111");
		Employee employee4 = Employee.create("Mike", "IT", 3500, "777-111-111");
		Employee employee5 = Employee.create("Linda", "Sales", 2000);
		EntityManager em = entityManagerFactory.createEntityManager();
		em.getTransaction().begin();
		em.persist(employee1);
		em.persist(employee2);
		em.persist(employee3);
		em.persist(employee4);
		em.persist(employee5);
		em.getTransaction().commit();

		System.out.println("-- Employees persisted --");
		Query query = em.createQuery("SELECT e FROM Employee e");
		List<Employee> resultList = query.getResultList();
		resultList.forEach(System.out::println);
		em.close();
	}

	@SuppressWarnings("unchecked")
	private static void findEmployeeByPhoneCount() {
		System.out.println("-- Employees who have more than 2 phones --");
		EntityManager em = entityManagerFactory.createEntityManager();
		Query query = em.createQuery("SELECT e FROM Employee e where (SELECT COUNT(p) FROM e.phoneNumbers p) >= 2");
		List<Employee> resultList = query.getResultList();
		resultList.forEach(System.out::println);
		em.close();
	}

	@SuppressWarnings("unchecked")
	private static void findDeptHavingAboveNetAverage() {
		EntityManager em = entityManagerFactory.createEntityManager();
		System.out.println("-- net average salary --");
		Object singleResult = em.createQuery("SELECT AVG(e.salary) FROM Employee e").getSingleResult();
		System.out.println(singleResult);

		System.out.println("-- Dept by average salaries --");
		List<Object[]> list = em.createQuery("SELECT e.dept, AVG(e.salary) FROM Employee e GROUP BY e.dept")
				.getResultList();
		list.forEach(ar -> System.out.println(Arrays.toString(ar)));

		System.out.println("-- Dept having AVG salaries greater than net AVG salary --");
		Query query = em
				.createQuery("SELECT e.dept, AVG(e.salary) FROM Employee e GROUP BY e.dept HAVING AVG(e.salary) > "
						+ "(SELECT AVG(e2.salary) FROM Employee e2)");
		List<Object[]> resultList = query.getResultList();
		resultList.forEach(r -> System.out.println(Arrays.toString(r)));
		em.close();
	}

	@SuppressWarnings("unchecked")
	private static void findEmployeeWithLessThanAverageSalary() {
		System.out.println("-- Employees who have less than average salary --");
		EntityManager em = entityManagerFactory.createEntityManager();
		Query query = em
				.createQuery("SELECT e FROM Employee e where  e.salary < (SELECT AVG(e2.salary) FROM Employee e2)");
		List<Employee> resultList = query.getResultList();
		resultList.forEach(System.out::println);
		em.close();
	}

}
