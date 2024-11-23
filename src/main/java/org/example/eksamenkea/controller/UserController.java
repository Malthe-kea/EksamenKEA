package org.example.eksamenkea.controller;

import jakarta.servlet.http.HttpSession;
import org.example.eksamenkea.model.User;
import org.example.eksamenkea.service.Errorhandling;
import org.example.eksamenkea.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("")
@Controller
public class UserController {
//  ('amalie@example.com', '123', 'PROJECTLEADER'),
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/") //hvad gør den??
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            model.addAttribute("userAvaliable", true);
            model.addAttribute("user", session.getAttribute("user"));
        } else {
            model.addAttribute("userAvaliable", false);
        }
        return "homepage";
    }

    @PostMapping("/validate_login")
    public String validateLogin(HttpSession session, @RequestParam String email, @RequestParam String password) throws Errorhandling {
        System.out.println(email + " " + password);
        User user = userService.signIn(email, password);
        if (user != null) {
            session.setAttribute("user", user);
          //  return "redirect:/logged_in";
          return "redirect:/";
        } else {
            return "login";
        }
    }
}