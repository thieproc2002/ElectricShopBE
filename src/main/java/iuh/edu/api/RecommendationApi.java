package iuh.edu.api;


import iuh.edu.entity.Product;
import iuh.edu.service.RecommendationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationApi {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<PagedResult<Product>> getRecommendedProducts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        List<Product> allRecommended = recommendationService.recommendProducts(userId);

        int totalItems = allRecommended.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int fromIndex = Math.min(page * size, totalItems);
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<Product> paged = allRecommended.subList(fromIndex, toIndex);

        PagedResult<Product> result = new PagedResult<>(
                paged,
                page,
                size,
                totalItems,
                totalPages
        );

        return ResponseEntity.ok(result);
    }
    @GetMapping("/nopage/{userId}")
    public ResponseEntity<List<Product>> getRecommendedProductsNoPage(@PathVariable Long userId) {
        List<Product> allRecommended = recommendationService.recommendProducts(userId);

        return ResponseEntity.ok(allRecommended);
    }

    @Data
    @AllArgsConstructor
    static class PagedResult<T> {
        private List<T> items;
        private int page;
        private int size;
        private int totalItems;
        private int totalPages;
    }
}
