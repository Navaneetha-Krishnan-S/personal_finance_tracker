package com.expensetracker.expensetracker.controllers;

import com.expensetracker.expensetracker.models.User;
import com.expensetracker.expensetracker.services.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignUp(@ModelAttribute User user, Model model) {
        // Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("errorMessage", "User already registered. Please log in.");
            return "signup";
        }

        // Save the new user if not already registered
        userRepository.save(user);
        model.addAttribute("successMessage", "Sign-up successful! Please log in.");
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            model.addAttribute("errorMessage", "Invalid username or password.");
            return "login";
        }

        session.setAttribute("user", user.get());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
