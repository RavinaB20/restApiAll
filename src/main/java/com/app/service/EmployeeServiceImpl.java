package com.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.custom_exceptions.ResourceNotFoundException;
import com.app.dto.EmpSalIncrementRequest;
import com.app.dto.EmployeeResponse;
import com.app.dto.EmployeeSpecificResp;
import com.app.dto.LoginRequestDto;
import com.app.pojos.Employee;
import com.app.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private ModelMapper mapper;
	
	@Override
	public List<Employee> getAllEmpDetails() {
		return empRepo.findAll();
	}

	@Override
	public Employee addEmpDetails(Employee transientEmp) {
		return empRepo.save(transientEmp);
	}

	@Override
	public String deleteEmpDetails(Long empId) {
		if (empRepo.existsById(empId)) {
			empRepo.deleteById(empId);
			return "Emp details deleted ....";
		}
		return "Deletion Failed : Invalid Emp Id !!!!!!!!!!!";
	}

	@Override
	public Employee fetchEmpDetails(Long empId) {
		return empRepo.findById(empId).orElseThrow(() -> new ResourceNotFoundException("Invalid Emp ID !!!!!"));
	}

	@Override
	public Employee updateEmpDetails(Employee detachedEmp) {
		if (empRepo.existsById(detachedEmp.getId())) {
			return empRepo.save(detachedEmp);
		}
		throw new ResourceNotFoundException("Invalid Emp Id : Updation Failed!!!!!!!!");
	}

	/*
	 * @Override public Employee authenticateEmp(LoginRequestDto dto) { // TODO
	 * Auto-generated method stub return
	 * empRepo.findByEmailAndPassword(dto.getEmail(), dto.getPassword())
	 * .orElseThrow(() -> new ResourceNotFoundException("Bad Credentials !!!!!")); }
	 */
		//OR
	@Override
	public EmployeeSpecificResp authenticateEmp(LoginRequestDto dto) {
		// TODO Auto-generated method stub
		Employee employee = empRepo.findByEmailAndPassword(dto.getEmail(), dto.getPassword())
				.orElseThrow(() -> new ResourceNotFoundException("Bad Credentials !!!!!"));
		//success entity to dto
		return mapper.map(employee, EmployeeSpecificResp.class);
		
	
	}
	
	@Override
	public List<Employee> getEmpsByDateAndDept(LocalDate joinDate1, String dept1) {
		return empRepo.findByJoinDateAfterAndDepartment(joinDate1, dept1);
	}

	@Override
	public List<EmployeeResponse> getEmpsBySalary(double minSal, double maxSal) {
		
//		return empRepo.fetchEmpNamesBySalaryRange(minSal, maxSal).//Stream<Emp>
//				map(e -> new EmployeeResponse(e.getFirstName(),e.getLastName())) //Stream<EmpResp>
//				.collect(Collectors.toList());
		return empRepo.fetchEmpNamesBySalaryRange(minSal, maxSal).//Stream<Emp>
				map(e -> mapper.map(e, EmployeeResponse.class)) //Entity --> DTO
						.collect(Collectors.toList());		 
	}

	@Override
	public List<Employee> getLastNameDependingOnString(String str) {
		// TODO Auto-generated method stub
		System.out.println( "HERE : "+empRepo.findByLastNameContaining(str));
		return empRepo.findByLastNameContaining(str);
	}

	@Override
	public String applySalaryIncrement(EmpSalIncrementRequest dto) {
		empRepo.findByDepartmentAndJoinDateBefore(dto.getDept(), dto.getSpecifiedDate()).forEach(e-> e.setSalary(e.getSalary()+dto.getSalIncrement()));
//OR
	//	int rows = empRepo.updateEmpSalary(dto.getSalIncrement(), dto.getDept(), dto.getSpecifiedDate());
		return "Applied salary increment";
	}

	
	
}
