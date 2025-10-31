package com.beautysalon.booking.controller;

import com.beautysalon.booking.entity.User;
import com.beautysalon.booking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // === Сторінка входу ===
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth_login"; // → templates/auth_login.html
    }

    // === Обробка логіну ===
    @PostMapping("/login")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        User user = userService.login(email, password);
        if (user != null) {
            // Зберігаємо користувача в сесії
            session.setAttribute("loggedInUser", user);
            return "redirect:/auth/home";
        } else {
            model.addAttribute("error", "Невірний email або пароль");
            return "auth_login";
        }
    }

    // === Сторінка реєстрації ===
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "register"; // → templates/register.html
    }

    // === Обробка реєстрації ===
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Якщо є помилки валідації (наприклад, порожні поля)
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        // Перевірка, чи email вже зайнятий
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Користувач з таким email вже існує!");
            model.addAttribute("user", user);
            return "register";
        }

        // Збереження користувача
        userService.save(user);

        // Повідомлення про успіх
        redirectAttributes.addFlashAttribute("success", "Реєстрація успішна! Увійдіть.");
        return "redirect:/auth/login";
    }

    // === Домашня сторінка після логіну ===
    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("user", loggedInUser);
            return "dashboard"; // → templates/dashboard.html
        } else {
            return "redirect:/auth/login";
        }
    }
}
