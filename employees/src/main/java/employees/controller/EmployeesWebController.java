package employees.controller;

import employees.service.CreateEmployeeCommand;
import employees.service.EmployeesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class EmployeesWebController {

    private EmployeesService employeesService;

    @GetMapping
    public ModelAndView listEmployees() {
        Map<String, Object> model = new HashMap<>();
        model.put("employees", employeesService.listEmployees());
        model.put("command", new CreateEmployeeCommand());

        return new ModelAndView("employees", model);
    }

    @PostMapping
    public ModelAndView createEmployee(@ModelAttribute CreateEmployeeCommand command) {
        employeesService.createEmployee(command);
        return new ModelAndView("redirect:/");
    }

}