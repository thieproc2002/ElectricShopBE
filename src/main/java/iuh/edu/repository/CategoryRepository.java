
package iuh.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iuh.edu.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

}