
package iuh.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import iuh.edu.entity.User;
import iuh.edu.repository.UserRepository;

@Controller
@RequestMapping("/forgot-password")
public class ResetPasswordController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("{token}")
    public String resetPassword(Model map, @PathVariable("token") String token) {
        User user = userRepository.findByToken(token);
        if (user == null) {
            return "redirect:/forgot-password/error";
        }
        map.addAttribute("name", user.getName());
        map.addAttribute("email", user.getEmail());
        map.addAttribute("token", token);
        map.addAttribute("password", "");
        map.addAttribute("confirm", "");
        return "/reset-password";
    }

    @PostMapping
    public String reset(@RequestParam("password") String password, @RequestParam("confirm") String confirm,
                        @RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("token") String token,
                        Model map) {
        if (password.length() < 6) {
            map.addAttribute("password", password);
            map.addAttribute("confirm", confirm);
            map.addAttribute("errorPassword", "error");
            map.addAttribute("name", name);
            map.addAttribute("email", email);
            map.addAttribute("token", token);
            return "/reset-password";
        }
        if (!password.equals(confirm)) {
            map.addAttribute("errorConfirm", "error");
            map.addAttribute("name", name);
            map.addAttribute("email", email);
            map.addAttribute("password", password);
            map.addAttribute("confirm", confirm);
            map.addAttribute("token", token);
            return "/reset-password";
        }
        User user = userRepository.findByToken(token);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(true);
        userRepository.save(user);
        return "redirect:/forgot-password/done";
    }

    @GetMapping("/done")
    public String done() {
        return "/done";
    }

    @GetMapping("/error")
    public String error() {
        return "/error";
    }

}