
package iuh.edu.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import iuh.edu.dto.ProductDTO;
import iuh.edu.entity.SearchHistory;
import iuh.edu.repository.SearchHistoryRepository;
import iuh.edu.repository.UserRepository;
import iuh.edu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import iuh.edu.entity.Category;
import iuh.edu.entity.Product;
import iuh.edu.repository.CategoryRepository;
import iuh.edu.repository.ProductRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("api/products")
public class ProductApi {

    @Autowired
    ProductRepository repo;
    @Autowired
    SearchHistoryRepository searchRepo;
    @Autowired
    CategoryRepository cRepo;
    @Autowired
    UserRepository uRepo;
    @Autowired
    ProductService productService;
    @GetMapping
    public ResponseEntity<Page<Product>> getAll(Pageable pageable) {
        return ResponseEntity.ok(repo.findByStatusTrue(pageable));
    }
    @GetMapping("/status")
    public ResponseEntity<List<Product>> getProductByStatus() {
        return ResponseEntity.ok(repo.findByStatusTrue());
    }
    @GetMapping("/nopage")
    public ResponseEntity<List<Product>> getAllNopage() {
        return ResponseEntity.ok(repo.findAll());
    }
    @GetMapping("bestseller")
    public ResponseEntity<List<Product>> getBestSeller() {
        return ResponseEntity.ok(repo.findByStatusTrueOrderBySoldDesc());
    }

    @GetMapping("bestseller-admin")
    public ResponseEntity<List<Product>> getBestSellerAdmin() {
        return ResponseEntity.ok(repo.findTop10ByOrderBySoldDesc());
    }

    @GetMapping("latest")
    public ResponseEntity<List<Product>> getLasted() {
        return ResponseEntity.ok(repo.findByStatusTrueOrderByEnteredDateDesc());
    }

    @GetMapping("rated")
    public ResponseEntity<List<Product>> getRated() {
        return ResponseEntity.ok(repo.findProductRated());
    }

    @GetMapping("suggest/{categoryId}/{productId}")
    public ResponseEntity<List<Product>> suggest(@PathVariable("categoryId") Long categoryId,
                                                 @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(repo.findProductSuggest(categoryId, productId, categoryId, categoryId));
    }

    @GetMapping("category/{id}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable("id") Long id) {
        if (!cRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Category c = cRepo.findById(id).get();
        return ResponseEntity.ok(repo.findByCategory(c));
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @PostMapping
    public ResponseEntity<Product> post(@RequestBody ProductDTO dto) {
//        if(repo.existsById(dto.getProductId())){
//            return ResponseEntity.badRequest().build();
//        }
        Product product = new Product();
//        product.setProductId(dto.getProductId());
        product.setName(dto.getName());
        product.setNormalizedName(productService.removeVietnameseAccent(dto.getName()));
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());
        product.setImage(dto.getImage());
        product.setDescription(dto.getDescription());
        product.setEnteredDate(dto.getEnteredDate() != null ? dto.getEnteredDate() : LocalDate.now());
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : true);
        product.setSold(dto.getSold());
        if (dto.getCategory() == null || dto.getCategory().getCategoryId() == null) {
            return ResponseEntity.badRequest().body(null); // thiếu category
        }

        Optional<Category> category = cRepo.findById(dto.getCategory().getCategoryId());
        if (!category.isPresent()) {
            return ResponseEntity.badRequest().body(null); // category không tồn tại
        }

        product.setCategory(category.get());

        return ResponseEntity.ok(repo.save(product));
    }

    @PutMapping("{id}")
    public ResponseEntity<Product> put(@PathVariable("id") Long id, @RequestBody ProductDTO dto) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
//        if (!id.equals(dto.getProductId())){
//            return ResponseEntity.badRequest().build();
//        }
        Optional<Product> optionalProduct = repo.findById(id);
        Product product = optionalProduct.get();
//        product.setProductId(dto.getProductId());
        product.setName(dto.getName());
        product.setNormalizedName(productService.removeVietnameseAccent(dto.getName()));
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());
        product.setImage(dto.getImage());
        product.setDescription(dto.getDescription());
        product.setEnteredDate(dto.getEnteredDate() != null ? dto.getEnteredDate() : product.getEnteredDate());
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : product.getStatus());
        product.setSold(dto.getSold());
        if (dto.getCategory() == null || dto.getCategory().getCategoryId() == null) {
            return ResponseEntity.badRequest().body(null); // thiếu category
        }

        Optional<Category> category = cRepo.findById(dto.getCategory().getCategoryId());
        if (!category.isPresent()) {
            return ResponseEntity.badRequest().body(null); // category không tồn tại
        }

        product.setCategory(category.get());

        return ResponseEntity.ok(repo.save(product));
    }

    @DeleteMapping("{product_id}")
    public ResponseEntity<String> delete(@PathVariable("product_id") Long id) {
        return repo.findById(id).map(product -> {
            try {
                // Cố gắng xóa sản phẩm
                repo.delete(product);
                return ResponseEntity.ok("Sản phẩm đã được xóa thành công.");
            } catch (Exception e) {
                // Nếu không thể xóa, đặt trạng thái ẩn
                product.setStatus(false);
                repo.save(product);
                return ResponseEntity.ok("Sản phẩm đã được ẩn do đã có đơn hàng liên quan.");
            }
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm với ID: " + id));
    }
    @PostMapping("/search/{userId}")
    public ResponseEntity<List<Product>> searchProducts(@PathVariable("userId") Long userId,
                                                        @RequestBody String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String processedKeyword = keyword.trim().toLowerCase();

        // Tìm kiếm dựa trên cả name và normalizedName
        List<Product> result = repo.findByStatusTrueAndNameContainingIgnoreCaseOrStatusTrueAndNormalizedNameContainingIgnoreCase(processedKeyword, processedKeyword);

        // Nếu không có kết quả, không lưu lịch sử
        if (result.isEmpty()) {
            return ResponseEntity.ok(result);
        }
        // Người dùng chưa đăng nhập trên fe sẽ mặc định gọi api với userid 0
        if (userId == 0) {
            return ResponseEntity.ok(result);
        }

        // Lưu hoặc cập nhật lịch sử tìm kiếm
        uRepo.findById(userId).ifPresent(user -> {
            Optional<SearchHistory> existing = searchRepo.findByUser_UserIdAndKeywordIgnoreCase(userId, processedKeyword);
            if (existing.isPresent()) {
                SearchHistory history = existing.get();
                history.setSearchedAt(LocalDateTime.now());
                searchRepo.save(history);
            } else {
                SearchHistory history = new SearchHistory();
                history.setUser(user);
                history.setKeyword(processedKeyword);
                history.setSearchedAt(LocalDateTime.now());
                searchRepo.save(history);
            }
        });

        return ResponseEntity.ok(result);
    }

}