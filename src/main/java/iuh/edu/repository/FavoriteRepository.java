
package iuh.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iuh.edu.entity.Favorite;
import iuh.edu.entity.Product;
import iuh.edu.entity.User;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);

    Integer countByProduct(Product product);

    Favorite findByProductAndUser(Product product, User user);

}
