
package iuh.edu.api;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import iuh.edu.entity.VerifyPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iuh.edu.config.JwtUtils;
import iuh.edu.dto.JwtResponse;
import iuh.edu.dto.LoginRequest;
import iuh.edu.dto.MessageResponse;
import iuh.edu.dto.SignupRequest;
import iuh.edu.entity.AppRole;
import iuh.edu.entity.Cart;
import iuh.edu.entity.User;
import iuh.edu.repository.AppRoleRepository;
import iuh.edu.repository.CartRepository;
import iuh.edu.repository.UserRepository;
import iuh.edu.service.SendMailService;
import iuh.edu.service.implement.UserDetailsImpl;

@CrossOrigin("*")
@RestController
@RequestMapping("api/auth")
public class UserApi {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AppRoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SendMailService sendMailService;

    @GetMapping
    public ResponseEntity<Page<User>> getAll(Pageable pageable) {
        return ResponseEntity.ok(userRepository.findByStatusTrue(pageable));
    }
    @GetMapping("nopage")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        User u = userRepository.findById(id).orElse(null);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }

        // Set status to false immediately
        u.setStatus(true);
        userRepository.save(u);


        return ResponseEntity.ok().build();
    }
    @GetMapping("{id}")
    public ResponseEntity<User> getOne(@PathVariable("id") Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userRepository.findById(id).get());
    }

    @GetMapping("email/{email}")
    public ResponseEntity<User> getOneByEmail(@PathVariable("email") String email) {
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.ok(userRepository.findByEmail(email).get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<User> post(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.notFound().build();
        }
        if (userRepository.existsById(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        Set<AppRole> roles = new HashSet<>();
        roles.add(new AppRole(1, null));

        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(jwtUtils.doGenerateToken(user.getEmail()));
        User u = userRepository.save(user);
        Cart c = new Cart(0L, 0.0, u.getAddress(), u.getPhone(), u);
        cartRepository.save(c);
        return ResponseEntity.ok(u);
    }
    @PostMapping("admin")
    public ResponseEntity<User> postad(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.notFound().build();
        }
        if (userRepository.existsById(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        Set<AppRole> roles = new HashSet<>();
        roles.add(new AppRole(2, null));

        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setToken(jwtUtils.doGenerateToken(user.getEmail()));
        User u = userRepository.save(user);
        Cart c = new Cart(0L, 0.0, u.getAddress(), u.getPhone(), u);
        cartRepository.save(c);
        return ResponseEntity.ok(u);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> put(@PathVariable("id") Long id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!id.equals(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        User temp = userRepository.findById(id).get();

        if (!user.getPassword().equals(temp.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Set<AppRole> roles = new HashSet<>();
        roles.add(new AppRole(1, null));

        user.setRoles(roles);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PutMapping("admin/{id}")
    public ResponseEntity<User> putAdmin(@PathVariable("id") Long id, @RequestBody User user) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!id.equals(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        Set<AppRole> roles = new HashSet<>();
        roles.add(new AppRole(2, null));

        user.setRoles(roles);
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        User u = userRepository.findById(id).get();
        u.setStatus(false);
        userRepository.save(u);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userDetails.getStatus()) {
            return ResponseEntity.badRequest().body("Tài khoản của bạn đã bị vô hiệu. Liên hệ admin để được hỗ trợ.");
        }
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getName(),
                userDetails.getEmail(), userDetails.getPassword(), userDetails.getPhone(), userDetails.getAddress(),
                userDetails.getGender(), userDetails.getStatus(), userDetails.getImage(), userDetails.getRegisterDate(),
                roles));

    }
    @GetMapping("/{id}/status")
    public ResponseEntity<Boolean> getUserStatus(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Assuming status is a boolean field in your User entity
        boolean status = user.getStatus();

        return ResponseEntity.ok(status);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is alreadv in use!"));
        }

        // create new user account
        User user = new User(signupRequest.getName(), signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getPhone(),
                signupRequest.getAddress(), signupRequest.getGender(), signupRequest.getStatus(),
                signupRequest.getImage(), signupRequest.getRegisterDate(),
                jwtUtils.doGenerateToken(signupRequest.getEmail()));
        Set<AppRole> roles = new HashSet<>();
        roles.add(new AppRole(1, null));

        user.setRoles(roles);
        userRepository.save(user);
        Cart c = new Cart(0L, 0.0, user.getAddress(), user.getPhone(), user);
        cartRepository.save(c);
        return ResponseEntity.ok(new MessageResponse("Đăng kí thành công"));

    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("send-mail-forgot-password-token")
    public ResponseEntity<String> sendToken(@RequestBody String email) {

        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }
        User user = userRepository.findByEmail(email).get();
        String token = user.getToken();
        sendMaiToken(email, token, "Reset mật khẩu");
        return ResponseEntity.ok().build();

    }

    public void sendMaiToken(String email, String token, String title) {
        String body = "\r\n" + "    <h2>Hãy nhấp vào link để thay đổi mật khẩu của bạn</h2>\r\n"
                + "    <a href=\"http://localhost:8080/forgot-password/" + token + "\">Đổi mật khẩu</a>";
        sendMailService.queue(email, title, body);
    }
    @PostMapping("/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody VerifyPasswordRequest request) {
        // Kiểm tra xem request.getUserId() và request.getOldPassword() có giá trị không null
        if (request.getUserId() == null || request.getOldPassword() == null) {
            return ResponseEntity.badRequest().body(false);
        }

        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Kiểm tra xem mật khẩu nguyên thủy (rawPassword) có giá trị không null trước khi so sánh
        String rawPassword = request.getOldPassword();
        if (rawPassword == null) {
            return ResponseEntity.badRequest().body(false);
        }

        // Thực hiện so sánh mật khẩu đã mã hóa với mật khẩu nguyên thủy
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }
}
