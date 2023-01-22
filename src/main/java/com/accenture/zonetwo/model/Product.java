package com.accenture.zonetwo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private Double price;

}
