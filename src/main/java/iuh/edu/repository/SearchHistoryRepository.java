package iuh.edu.repository;

import iuh.edu.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findTop3ByUser_UserIdOrderBySearchedAtDesc(Long userId);
    Optional<SearchHistory> findByUser_UserIdAndKeywordIgnoreCase(Long userId, String keyword);

}
