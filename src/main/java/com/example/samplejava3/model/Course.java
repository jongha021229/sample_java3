package com.example.samplejava3.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class Course {

    @NotBlank
    private String title;

    @NotNull
    @Positive
    private Double fee;

    private String description;

    public Course() {
    }

    public Course(String title, Double fee, String description) {
        this.title = title;
        this.fee = fee;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
