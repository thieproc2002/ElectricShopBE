
package iuh.edu.api;

import java.util.List;
import java.util.Optional;

import iuh.edu.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iuh.edu.entity.CartDetail;
import iuh.edu.entity.Product;
import iuh.edu.repository.CartDetailRepository;
import iuh.edu.repository.CartRepository;
import iuh.edu.repository.ProductRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("api/cartDetail")
public class CartDetailApi {

    @Autowired
    CartDetailRepository cartDetailRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

//    @GetMapping("cart/{userid}")
//    public ResponseEntity<List<CartDetail>> getByCartId(@PathVariable("userid") Long userid) {
//        Cart cart = cartRepository.findByUser_UserId(userid);
//        if (cart == null) {
//            return ResponseEntity.notFound().build();
//        }
//        List<CartDetail> cartDetails = cartDetailRepository.findByCart(cart);
//        return ResponseEntity.ok(cartDetails);
//    }
    @GetMapping("cart/{id}")
    public ResponseEntity<List<CartDetail>> getByCartId(@PathVariable("id") Long id) {
        if (!cartRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartDetailRepository.findByCart(cartRepository.findById(id).get()));
}
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<CartDetail> getOne(@PathVariable("id") Long id) {
        if (!cartDetailRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartDetailRepository.findById(id).get());
    }

    @PostMapping()
    public ResponseEntity<CartDetail> post(@RequestBody CartDetail detail) {
        Optional<Cart> cartOpt = cartRepository.findById(detail.getCart().getCartId());
        if (!cartOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Product> productOpt = productRepository.findById(detail.getProduct().getProductId());
        if (!productOpt.isPresent() || !productOpt.get().isStatus()) {
            return ResponseEntity.notFound().build();
        }

        Cart cart = cartOpt.get();
        Product product = productOpt.get();

        // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        List<CartDetail> listD = cartDetailRepository.findByCart(cart);
        for (CartDetail item : listD) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                item.setQuantity(item.getQuantity() + 1);
                item.setPrice(item.getPrice() + detail.getPrice());
                return ResponseEntity.ok(cartDetailRepository.save(item));
            }
        }
        // Nếu sản phẩm chưa có trong giỏ, thêm mới vào giỏ hàng
        detail.setCart(cart);
        detail.setProduct(product);
        return ResponseEntity.ok(cartDetailRepository.save(detail));
    }

    @PutMapping()
    public ResponseEntity<CartDetail> put(@RequestBody CartDetail detail) {
        if (!cartRepository.existsById(detail.getCart().getCartId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartDetailRepository.save(detail));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!cartDetailRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cartDetailRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
