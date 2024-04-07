package com.funiber.inventory.infrastructure.repository.DB;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.funiber.inventory.infrastructure.entity.inventory.InventoryEntity;

@Repository
public interface InventoryRepositoryDB extends JpaRepository<InventoryEntity, Long> {
    @Query(value = " SELECT i.* FROM inventory i " +
            "JOIN products p ON p.id  = i.product_id " +
            " WHERE p.id = :#{#code} AND i.is_active = 'Y'  ", nativeQuery = true)
    Optional<InventoryEntity> findInventoryActiveByProductCode(@Param("code") Long id);

    @Query(value = " SELECT i.* FROM inventory i " +
            " WHERE i.product_id = :#{#id} ORDER BY i.created_at DESC ", countQuery = " SELECT COUNT(*) FROM inventory i " +
                    " WHERE i.product_id = :#{#id} ", nativeQuery = true)
    Page<InventoryEntity> ListHistoricInventoryByProductCode(@Param("id") Long id, Pageable pageable);

}
