/*
* This class is a REST controller that handles HTTP requests related to products in the inventory.
* It provides endpoints for listing, retrieving, creating, updating, and deleting products.
* The controller uses the ProductService to perform business logic and data access operations.
*/

package com.example.inventory.controller;

import com.example.inventory.dto.ProductRequest;
import com.example.inventory.dto.ProductResponse;
import com.example.inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// RestController annotation indicates that this class is a RESTful controller in the Spring context.
// RequestMapping annotation specifies the base URL path for all endpoints in this controller.
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    /*
     * Lists all products in the inventory.
     * @return A list of ProductResponse objects representing the products.
     */
    @GetMapping
    public List<ProductResponse> list() {
        return service.list();
    }

    /*
     * Retrieves a product by its ID.
     * @param id The ID of the product to retrieve.
     * @return The ProductResponse object representing the product.
     */
    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable Long id) {
        return service.get(id);
    }
    /*
     * Creates a new product in the inventory.
     * @param request The request object containing product details.
     * @return The ProductResponse object representing the created product.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return service.create(request);
    }
    /*
     * Updates an existing product in the inventory.
     * @param id The ID of the product to update.
     * @param request The request object containing updated product details.
     * @return The ProductResponse object representing the updated product.
     */
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return service.update(id, request);
    }

    /*
     * Deletes a product from the inventory.
     * @param id The ID of the product to delete.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
