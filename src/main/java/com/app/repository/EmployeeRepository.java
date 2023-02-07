package com.app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.app.pojos.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Optional<Employee> findByEmailAndPassword(String em, String pass);

	List<Employee> findByJoinDateAfterAndDepartment(LocalDate date, String deptName);

	List<Employee> findByLastNameContaining(String str);
	
	@Query(value = "select new com.app.pojos.Employee(firstName,lastName) from Employee e where e.salary between ?1 and ?2")
	// List<Employee> fetchEmpNamesBySalaryRange(double minSalary,double maxSalary);
	Stream<Employee> fetchEmpNamesBySalaryRange(double minSalary, double maxSalary);

	List<Employee> findByDepartmentAndJoinDateBefore(String dept, LocalDate date);
	
	/*
	 * @Modifying
	 * 
	 * @Query("update Employee u set u.salary=e.salary+?1 where u.department=?2 and joidDate < ?3"
	 * ) int updateEmpSalary(double salIncr, String dept, LocalDate date);
	 */
	
	
}
