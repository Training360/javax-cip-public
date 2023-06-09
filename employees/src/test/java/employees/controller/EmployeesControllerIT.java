package employees.controller;

import employees.service.CreateEmployeeCommand;
import employees.service.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeesControllerIT {

    @Autowired
    WebTestClient webClient;

    @Test
    void createEmployee() {
        webClient
                .post()
                .uri("/api/employees")
                .bodyValue(new CreateEmployeeCommand("John Doe"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDto.class).value(e -> assertEquals("John Doe", e.getName()));
    }
}
