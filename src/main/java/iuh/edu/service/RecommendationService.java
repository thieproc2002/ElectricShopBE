package iuh.edu.service;

import iuh.edu.entity.Product;
import iuh.edu.entity.SearchHistory;
import iuh.edu.repository.OrderDetailRepository;
import iuh.edu.repository.ProductRepository;
import iuh.edu.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final SearchHistoryRepository searchHistoryRepo;
    private final ProductRepository productRepo;

    public List<Product> recommendProducts(Long userId) {
        // Lấy lịch sử từ khóa
        List<SearchHistory> history = searchHistoryRepo.findTop3ByUser_UserIdOrderBySearchedAtDesc(userId);

        // Tìm sản phẩm liên quan đến từ khóa
        Set<Product> recommended = new HashSet<>();
        for (SearchHistory h : history) {
            List<Product> matches = productRepo.findByStatusTrueAndNameContainingIgnoreCaseOrStatusTrueAndNormalizedNameContainingIgnoreCase(h.getKeyword(),h.getKeyword());
            recommended.addAll(matches);
        }


        return recommended.stream()
                .sorted(Comparator.comparingInt(Product::getSold).reversed())
                .collect(Collectors.toList());

    }
}
