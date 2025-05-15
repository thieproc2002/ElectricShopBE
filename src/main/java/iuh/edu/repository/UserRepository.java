
package iuh.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iuh.edu.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByStatusTrue(Pageable pageable);
List<User> findByStatusTrue();
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String username);

    User findByToken(String token);

}
