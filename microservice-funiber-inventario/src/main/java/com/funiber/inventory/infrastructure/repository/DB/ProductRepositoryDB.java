package com.funiber.inventory.infrastructure.repository.DB;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.funiber.inventory.infrastructure.entity.inventory.ProductEntity;

@Repository
public interface ProductRepositoryDB
                extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
        @Query(value = "SELECT c.id, c.name, ih.created_at, ih.created_by, ih.height, ih.is_active, ih.length, " +
                        "ih.stock_quantity, ih.width, ih.id, p.code, p.name FROM products p " +
                        "JOIN inventory ih ON p.id = ih.product_id " +
                        "JOIN categories c ON c.id = p.category_id " +
                        "WHERE p.id = :#{#id} AND ih.is_active = 'Y'", nativeQuery = true)
        List<Object[]> findProductByIdTableWithInventory(@Param("id") Long id);

        @Query(value = "SELECT c.id, c.name, ih.created_at, ih.created_by, ih.height, ih.is_active, ih.length, " +
                        "ih.stock_quantity, ih.width, ih.id, p.code, p.name, p.id, c.code, c.is_active,ih.updated_at, ih.updated_by "
                        + "FROM products p " +
                        "JOIN inventory ih ON p.id = ih.product_id " +
                        "JOIN categories c ON c.id = p.category_id " +
                        "WHERE ih.is_active = 'Y' ORDER BY ih.created_at DESC", countQuery = "SELECT count(*) " +
                                        "FROM products p " +
                                        "JOIN inventory ih ON p.id = ih.product_id " +
                                        "JOIN categories c ON c.id = p.category_id " +
                                        "WHERE ih.is_active = 'Y'", nativeQuery = true)
        Page<Object[]> getProductsWithInventory(Pageable pageable);

        @Query(value = "SELECT * " +
                        " FROM products p " +
                        "WHERE p.code = :#{#id}", nativeQuery = true)
        Optional<ProductEntity> findProductByCode(@Param("id") String id);

        @Query(value = "SELECT p.* FROM products p " +
                        "JOIN categories  c ON c.id = p.category_id " +
                        "WHERE ((:code IS NULL or :code = '' ) OR p.code LIKE %:code%) " +
                        "AND ((:name IS NULL or :name = '') OR p.name LIKE %:name%) " +
                        "AND ((:category IS NULL or :category = '') OR c.name LIKE %:category%) ", countQuery = "SELECT COUNT(*) FROM products p "
                                        +
                                        "JOIN categories c ON c.id = p.category_id " +
                                        "WHERE ((:code IS NULL or :code = '' ) IS NULL OR p.code LIKE  %:code%) " +
                                        "AND ((:name IS NULL or :name = '') OR p.name LIKE %:name% ) " +
                                        "AND ((:category IS NULL or :category = '') OR c.name LIKE %:category% ) ", nativeQuery = true)
        Page<ProductEntity> searchProductsByParams(@Param("code") String code,
                        @Param("name") String name,
                        @Param("category") String category,
                        Pageable pageable);

}
