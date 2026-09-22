package com.rikkei.productservice.service;

import com.rikkei.productservice.dto.ProductRequestDTO;
import com.rikkei.productservice.dto.ProductResponseDTO;
import com.rikkei.productservice.entity.Product;
import com.rikkei.productservice.exception.ProductNotFoundException;
import com.rikkei.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Product product = Product.builder()
                .name(requestDTO.getName())
                .price(requestDTO.getPrice())
                .stockQuantity(requestDTO.getStockQuantity())
                .description(requestDTO.getDescription())
                .build();

        Product savedProduct = productRepository.save(product);
        return mapToResponseDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Không tìm thấy sản phẩm có ID: " + id));
        return mapToResponseDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .build();
    }
}
