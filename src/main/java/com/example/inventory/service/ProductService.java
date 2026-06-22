/*
* ProductService interface defining the contract for product-related operations in the inventory system.
* Basic CRUD operations are defined here
*/
package com.example.inventory.service;

import com.example.inventory.dto.ProductRequest;
import com.example.inventory.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> list();

    ProductResponse get(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);
}
