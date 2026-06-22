/*
* ProductServiceImpl class implementing the ProductService interface.
* This class provides the actual business logic for managing products in the inventory system.
*/

package com.example.inventory.service;

import com.example.inventory.dto.ProductRequest;
import com.example.inventory.dto.ProductResponse;
import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.model.Product;
import com.example.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Service annotation indicates that this class is a service component in the Spring context.
// Transactional annotation ensures that the methods in this class are executed within a transaction context.
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    /*
     * Lists all products in the inventory.
     *
     * @return A list of ProductResponse objects representing the products.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /*
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return The ProductResponse object representing the product.
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(Long id) {
        return toResponse(findOrThrow(id));
    }

    /*
     * Creates a new product in the inventory.
     *
     * @param request The request object containing the product details.
     * @return The ProductResponse object representing the created product.
     */
    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        copyData(request, product);
        return toResponse(repository.save(product));
    }

    /*
     * Updates an existing product in the inventory.
     *
     * @param id The ID of the product to update.
     * @param request The request object containing the updated product details.
     * @return The ProductResponse object representing the updated product.
     */
    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = findOrThrow(id);
        copyData(request, product);
        return toResponse(repository.save(product));
    }

    /*
     * Deletes a product from the inventory.
     *
     * @param id The ID of the product to delete.
     */
    @Override
    public void delete(Long id) {
        Product product = findOrThrow(id);
        repository.delete(product);
    }

    private Product findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    /*
     * Copies data from a ProductRequest object to a Product entity.
     *
     * @param request The request object containing the product details.
     * @param product The product entity to update.
     */
    private void copyData(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());
        product.setUnitPrice(request.getUnitPrice());
    }

    /*
     * Converts a Product entity to a ProductResponse object.
     *
     * @param product The product entity to convert.
     * @return The ProductResponse object representing the product.
     */
    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getQuantity(),
                product.getUnitPrice()
        );
    }
}
