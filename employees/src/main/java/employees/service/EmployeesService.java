package employees.service;

import employees.controller.EmployeesController;
import employees.repository.Employee;
import employees.repository.EmployeesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EmployeesService {

    private EmployeeMapper employeeMapper;

    private EmployeesRepository repository;

    public List<EmployeeDto> listEmployees() {
        return employeeMapper.map(repository.findAll());
    }

    public EmployeeDto findEmployeeById(long id) {
        return employeeMapper.map(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("employee not found")));
    }

    public EmployeeDto createEmployee(CreateEmployeeCommand command) {
        Employee employee = new Employee(command.getName());
        repository.save(employee);
        log.info("Employee has been created");
        log.debug("Employee has been created with name {}", command.getName());
        return employeeMapper.map(employee);
    }

    @Transactional
    public EmployeeDto updateEmployee(long id, UpdateEmployeeCommand command) {
        Employee employee = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("employee not found"));
        employee.setName(command.getName());
        return employeeMapper.map(employee);
    }

    public void deleteEmployee(long id) {
        repository.deleteById(id);
    }

    public void deleteAllEmployees() {
        repository.deleteAll();
    }
}
