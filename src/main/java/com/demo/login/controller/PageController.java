package com.demo.login.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles page navigation.
 *
 * GET /login     → show the login form
 * GET /dashboard → show the dashboard (only reachable when logged in)
 */
@Controller
public class PageController {

    /**
     * Shows the login page.
     * Spring Security automatically posts the form to /login (POST),
     * so we only need to handle the GET here.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // resolves to src/main/resources/templates/login.html
    }

    /**
     * Shows the dashboard after a successful login.
     *
     * The Authentication object is injected by Spring Security automatically.
     * It contains the logged-in user's details (name, roles, etc.).
     */
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        // Pass the username to the template so we can greet the user
        model.addAttribute("username", authentication.getName());

        // Pass the user's roles so the template can display them
        model.addAttribute("roles", authentication.getAuthorities());

        return "dashboard"; // resolves to src/main/resources/templates/dashboard.html
    }

}
