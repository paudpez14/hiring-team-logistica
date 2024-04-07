package com.funiber.inventory.infrastructure.entity.inventory;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.context.annotation.DependsOn;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity(name = "categories")
@Table(name = "categories")
@DependsOn("products")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<ProductEntity> products;
    @Column(nullable = false)
    private String isActive = "Y"; // Valor por defecto
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
