package employees.service;

import employees.repository.Employee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeDto map(Employee employee);

    List<EmployeeDto> map(List<Employee> employee);
}
