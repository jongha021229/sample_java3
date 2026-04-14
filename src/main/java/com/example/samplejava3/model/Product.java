package com.example.samplejava3.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Product {

    @NotBlank(message = "상품명은 필수입니다")
    @Size(min = 1, max = 200, message = "상품명은 1~200자여야 합니다")
    private String name;

    @NotBlank(message = "카테고리는 필수입니다")
    @Size(min = 1, max = 100, message = "카테고리는 1~100자여야 합니다")
    private String category;

    @NotNull(message = "가격은 필수입니다")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다")
    private Integer price;

    @Size(max = 500, message = "설명은 500자 이하여야 합니다")
    private String description;

    public Product() {
    }

    public Product(String name, String category, Integer price, String description) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
