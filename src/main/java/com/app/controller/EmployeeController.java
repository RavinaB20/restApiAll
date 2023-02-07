package com.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ApiResponse;
import com.app.dto.EmpSalIncrementRequest;
import com.app.dto.EmployeeResponse;
import com.app.dto.LoginRequestDto;
import com.app.pojos.Employee;
import com.app.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	@Autowired
	private EmployeeService empService;

	@GetMapping
	public List<Employee> getAllEmps() {
		return empService.getAllEmpDetails();
	}

	@PostMapping
	public Employee saveEmpDetails(@RequestBody Employee transientEmp) {
		return empService.addEmpDetails(transientEmp);
	}

	public ApiResponse deleteEmpDetails(@PathVariable Long empId) {
		return new ApiResponse(empService.deleteEmpDetails(empId));
	}

	@GetMapping("/{empId}")
	public Employee getEmpDetails(@PathVariable Long empId) {
		return empService.fetchEmpDetails(empId);
	}

	@PutMapping
	public Employee updateEmpDetails(@RequestBody Employee detachedEmp) {
		return empService.updateEmpDetails(detachedEmp);
	}

	@GetMapping("/date/{joinDate1}/dept/{dept1}")
	public List<Employee> getAllEmpsByDateAndDept(
			@PathVariable @DateTimeFormat(pattern = "yyyy-M-d") LocalDate joinDate1, @PathVariable String dept1) {
		return empService.getEmpsByDateAndDept(joinDate1, dept1);
	}

	@GetMapping("/salary")
	public List<EmployeeResponse> getAllEmpsBySalaryRange(@RequestParam double minSal, double maxSal) {
		return empService.getEmpsBySalary(minSal, maxSal);
	}

	@PutMapping("/sal_inc")
	public ApiResponse applySalaryIncreament(@RequestBody EmpSalIncrementRequest dto) {
		return new ApiResponse(empService.applySalaryIncrement(dto));
	}

	/*
	 * @GetMapping("/lastname/{str}") public List<Employee>
	 * getLastNameDependingOnString(@PathVariable String str){
	 * 
	 * return empService.getLastNameDependingOnString(str); }
	 */

	@GetMapping("/lastname/{str}")
	public ResponseEntity<?> getLastNameDependingOnString(@PathVariable String str) {
		List<Employee> list = empService.getLastNameDependingOnString(str);
		if (list.isEmpty())
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		else
			return ResponseEntity.ok(list);
	}

	/*
	 * @PostMapping("/signin") public Employee validateEmployee(@RequestBody
	 * LoginRequestDto dto) { System.out.println("in emp signin "+dto); return
	 * empService.authenticateEmp(dto); }
	 */

	/*
	 * @PostMapping("/signin") public ResponseEntity<?>
	 * validateEmployee(@RequestBody LoginRequestDto dto) {
	 * System.out.println("in emp signin "+dto); Employee emp = new Employee(); try{
	 * return ResponseEntity.ok(empService.authenticateEmp(dto)); } catch
	 * (RuntimeException e) { System.out.println("In emp controller : "+e); return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).body(new
	 * ApiResponse(e.getMessage())); } }
	 */

	@PostMapping("/signin")
	public ResponseEntity<?> validateEmployee(@RequestBody LoginRequestDto dto) {
		System.out.println("in emp signin " + dto);
		Employee emp = new Employee();
		try {
			return ResponseEntity.ok(empService.authenticateEmp(dto));
		} catch (RuntimeException e) {
			System.out.println("In emp controller : " + e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage()));
		}
	}
}
