
package iuh.edu.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iuh.edu.entity.OrderDetail;
import iuh.edu.repository.OrderDetailRepository;
import iuh.edu.repository.OrderRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("api/orderDetail")
public class OderDetailApi {

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderDetail>> getByOrder(@PathVariable("id") Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDetailRepository.findByOrder(orderRepository.findById(id).get()));
    }

}
