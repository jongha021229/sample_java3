package com.example.samplejava3.controller;

import com.example.samplejava3.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
@Validated
public class ProductController {

    private static final Logger logger = Logger.getLogger(ProductController.class.getName());

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @GetMapping
    public List<Map<String, Object>> listProducts() {
        List<Map<String, Object>> result = new ArrayList<>();
        products.forEach((id, product) -> result.add(productToMap(id, product)));
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable Long id) {
        Product product = products.get(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "not found"));
        }
        return ResponseEntity.ok(productToMap(id, product));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody Product product) {
        long id = nextId.getAndIncrement();
        products.put(id, product);
        logger.info("상품 등록: " + product.getName() + " (카테고리: " + product.getCategory() + ")");
        return ResponseEntity.status(HttpStatus.CREATED).body(productToMap(id, product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
        if (!products.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "not found"));
        }
        products.put(id, product);
        logger.info("상품 수정: id=" + id + ", name=" + product.getName());
        return ResponseEntity.ok(productToMap(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        Product removed = products.remove(id);
        if (removed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "not found"));
        }
        logger.info("상품 삭제: id=" + id);
        return ResponseEntity.ok(Map.of("status", "deleted"));
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchProducts(
            @RequestParam(defaultValue = "")
            @Size(max = 100, message = "검색어는 100자 이하여야 합니다") String q) {
        List<Map<String, Object>> result = new ArrayList<>();
        String query = q.toLowerCase();
        logger.info("상품 검색: q=" + q);
        products.forEach((id, product) -> {
            if (product.getName().toLowerCase().contains(query)
                    || product.getCategory().toLowerCase().contains(query)) {
                result.add(productToMap(id, product));
            }
        });
        return result;
    }

    private Map<String, Object> productToMap(Long id, Product product) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("name", product.getName());
        map.put("category", product.getCategory());
        map.put("price", product.getPrice());
        map.put("description", product.getDescription());
        return map;
    }
}
