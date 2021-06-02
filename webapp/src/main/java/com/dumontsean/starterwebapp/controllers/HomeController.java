package com.dumontsean.starterwebapp.controllers;

import com.dumontsean.data.entities.User;
import com.dumontsean.services.registries.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController extends AbstractController {

    private final UserRegistry userRegistry;

    @Autowired
    public HomeController(UserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<User> allUsers = userRegistry.findAll();
        log.error("All users from Get: " + allUsers);
        model.addAttribute("users", allUsers);
        return "home";
    }

    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addUser(User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }

        userRegistry.save(user);
        return "redirect:/home";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Optional<User> user = userRegistry.findById(id);
        User userObj = user.orElse(null);
        model.addAttribute("user", userObj);

        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        userRegistry.save(user);

        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        Optional<User> user = userRegistry.findById(id);
        user.ifPresent(userRegistry::delete);

        return "redirect:/home";
    }
    //create a core and services module. the core module will be for the application configuration and the services module will be for the DB services objects that'll feed the DTOs and stuff

    @GetMapping(value = "error")
    public String getErrorPage(HttpServletRequest request, Model model) {
        log.error("Yes this is error Sean");
        model.addAttribute(ERR_MSG_ATTR, String.valueOf(request.getParameter("errorMessage")));
        return "home/error";
    }
}
