package com.funiber.inventory.infrastructure.repository.DB;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.funiber.inventory.infrastructure.entity.inventory.CategoryEntity;

@Repository
public interface CategoryRepositoryDB
        extends JpaRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity> {

    @Query(value = "SELECT CASE " +
            "WHEN EXISTS(" +
            "SELECT 1 FROM categories " +
            "WHERE categories.code = :#{#code} " +
            "and is_active = 'Y') " +
            "THEN 'TRUE' " +
            "ELSE 'FALSE' END " +
            "FROM DUAL", nativeQuery = true)
    boolean checkCategoryExists(@Param("code") String code);

    @Query(value = "SELECT * " +
            " FROM categories c " +
            "WHERE c.code = :#{#id}", nativeQuery = true)
    Optional<CategoryEntity> findCategoryByCode(@Param("id") String id);
}
