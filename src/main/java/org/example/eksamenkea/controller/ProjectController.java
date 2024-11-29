package org.example.eksamenkea.controller;

import jakarta.servlet.http.HttpSession;
import org.example.eksamenkea.model.*;
import org.example.eksamenkea.service.EmployeeService;
import org.example.eksamenkea.service.Errorhandling;
import org.example.eksamenkea.service.ProjectService;
import org.example.eksamenkea.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProjectController {
    private ProjectService projectService;
    private TaskService taskService;
    private EmployeeService employeeService;

    public ProjectController(ProjectService projectService, TaskService taskService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.employeeService = employeeService;
    }

    @GetMapping("/project-leader-overview")
    public String showProjectLeaderOverview(HttpSession session, Model model) throws Errorhandling {
        // Hent brugerens rolle og medarbejderdata fra sessionen
        Role userRole = (Role) session.getAttribute("userRole");
        Employee employee = (Employee) session.getAttribute("employee");

        // Debugging
        System.out.println("Employee in session: " + employee);
        System.out.println("UserRole in session: " + userRole);

        // Tjek om brugeren er en projektleder
        if (userRole == Role.PROJECTLEADER) {
            // Hent projekter tilknyttet projektlederen
            List<Project> projects = projectService.getAllProjectsByEmployeeId(employee.getEmployee_id());

            // Tilføj projekter til modellen, så de kan vises i HTML'en
            model.addAttribute("projects", projects);

            // Returnér navnet på Thymeleaf-skabelonen
            return "project-leader-overview";
        }

        // Kaster fejl, hvis brugeren ikke er autoriseret
        throw new Errorhandling("User is not authorized to view this page.");
    }

    @GetMapping("/project-leader-subproject-overview") // Amalie
    public String showProjectLeaderSubprojectOverview(@RequestParam("projectName") String projectName,
                                                      HttpSession session,
                                                      Model model) throws Errorhandling {
        Role employeeRole = (Role) session.getAttribute("userRole"); // Henter brugerens rolle fra sessionen
        Employee employee = (Employee) session.getAttribute("employee");

        if (employeeRole == Role.PROJECTLEADER) {
            // Hent projectId baseret på projectName
            int projectId = projectService.getProjectIdByProjectName(projectName);


            // Henter subprojekter baseret på det fundne projectId
            List<Subproject> subprojects = projectService.getSubjectsByProjectId(projectId);
            model.addAttribute("subprojects", subprojects);
            model.addAttribute("projectName", projectName);

            return "project-leader-subproject-overview"; // Returnerer view
        }
        throw new Errorhandling("User is not authorized to view this page.");
    }


    @GetMapping("/add-project") //Amalie
    public String addNewProject(HttpSession session, Model model) throws Errorhandling {
        Project project = new Project();
        Role userRole = (Role) session.getAttribute("userRole");  // Henter "userrole" fra sessionen.
        Employee employee = (Employee) session.getAttribute("employee");  // Henter "user" fra sessionen.
        System.out.println("Employee in session: " + session.getAttribute("employee"));

        if (userRole == Role.PROJECTLEADER) {
            model.addAttribute("project", project);
            model.addAttribute("employeeId", employee.getEmployee_id());
            return "add-project-form";
        }
        throw new Errorhandling("cant add project");
    }

    @PostMapping("/project-added") //Amalie
    public String addedProject(@ModelAttribute Project project) throws Errorhandling {
        projectService.addProject(project);
        return "redirect:/project-leader-overview";
    }

    @GetMapping("/worker-overview")
    public String showWorkerOverview(HttpSession session, Model model) throws Errorhandling {
        Role employeeRole = (Role) session.getAttribute("userRole");
        Employee employee = (Employee) session.getAttribute("employee");

        if (employeeRole == Role.WORKER) {
            Project project = projectService.getWorkerProjectFromEmployeeId(employee.getEmployee_id());
            List<Subproject> subprojects = projectService.getSubjectsByProjectId(project.getProject_id());
            List<Task> taskList = taskService.getTasklistByEmployeeId(employee.getEmployee_id());

            model.addAttribute("project", project);
            model.addAttribute("employee", employee);
            model.addAttribute("subprojects", subprojects);
            model.addAttribute("tasklist", taskList);

            return "worker-overview";
        }
        throw new Errorhandling("User is not authorized to view this page.");
    }


}
