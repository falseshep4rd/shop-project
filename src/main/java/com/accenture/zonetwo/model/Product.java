package com.accenture.zonetwo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;
    @NonNull
    @NotEmpty
    private String name;
    @NonNull
    private Double price;
}
