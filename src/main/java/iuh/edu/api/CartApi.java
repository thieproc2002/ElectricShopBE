
package iuh.edu.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iuh.edu.entity.Cart;
import iuh.edu.repository.CartDetailRepository;
import iuh.edu.repository.CartRepository;
import iuh.edu.repository.UserRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("api/cart")
public class CartApi {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartDetailRepository cartDetailRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/{email}")
    public ResponseEntity<Cart> getCartUser(@PathVariable("email") String email) {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartRepository.findByUser(userRepository.findByEmail(email).get()));
    }

    @PutMapping("/user/{email}")
    public ResponseEntity<Cart> putCartUser(@PathVariable("email") String email, @RequestBody Cart cart) {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartRepository.save(cart));
    }

}
