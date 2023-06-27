package employees.controller;

import employees.service.CreateEmployeeCommand;
import employees.service.EmployeeDto;
import employees.service.EmployeesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeesControllerTest {

    @Mock
    EmployeesService employeesService;

    @InjectMocks
    EmployeesController employeesController;

    @Test
    void testCreateEmployee() {
        when(employeesService.createEmployee(any())).thenReturn(new EmployeeDto(1L, "John Doe"));

        UriComponentsBuilder builder = mock(UriComponentsBuilder.class);
        UriComponents components = mock(UriComponents.class);
        when(builder.path(any())).thenReturn(builder);
        when(builder.buildAndExpand(anyLong())).thenReturn(components);

        EmployeeDto employeeDto = employeesController.createEmployee(new CreateEmployeeCommand("John Doe"), builder).getBody();

        verify(employeesService).createEmployee(argThat(command -> command.getName().equals("John Doe")));
        assertEquals(1L, employeeDto.getId());
        assertEquals("John Doe", employeeDto.getName());
    }

}
