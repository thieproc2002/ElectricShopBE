
package iuh.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iuh.edu.entity.OrderDetail;
import iuh.edu.entity.Product;
import iuh.edu.entity.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    List<Rate> findAllByOrderByIdDesc();

    Rate findByOrderDetail(OrderDetail orderDetail);

    List<Rate> findByProductOrderByIdDesc(Product product);

}
