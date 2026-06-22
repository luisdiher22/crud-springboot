/*
* This class represents a response containing product details.
* It is used to send product information back to the client after a successful operation.
* It contains fields such as id, name, description, quantity, and unit price.
* Similar to a toString method, this class provides a convenient way to represent product data in API responses.
*/

package com.example.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
}
