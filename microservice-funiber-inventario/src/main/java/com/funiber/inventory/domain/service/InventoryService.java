package com.funiber.inventory.domain.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.funiber.inventory.application.dto.response.InventoryRespDTO;
import com.funiber.inventory.application.dto.response.PageDTO;
import com.funiber.inventory.domain.dto.Category;
import com.funiber.inventory.domain.dto.Inventory;
import com.funiber.inventory.domain.dto.NewCategory;
import com.funiber.inventory.domain.dto.NewInventory;
import com.funiber.inventory.domain.dto.NewProduct;
import com.funiber.inventory.domain.dto.Product;
import com.funiber.inventory.domain.dto.ProductResume;
import com.funiber.inventory.domain.dto.UpdateInventory;
import com.funiber.inventory.domain.dto.UpdateProduct;
import com.funiber.inventory.domain.dto.UpdatedCategory;

public interface InventoryService {

    ProductResume createProduct(NewProduct product);

    PageDTO<ProductResume> getAllProductsResume(Integer pageNumber, Integer pageSize, String type);

    Optional<ProductResume> getProductByCodeWithInventory(String id);

    Product getProductByCode(String id);

    Product updateProduct(String id, UpdateProduct updateProduct);

    void deleteProduct(String id);

    PageDTO<ProductResume> searchProductsByParams(String code, String name, String category, Boolean hasStock, int page,
            int size);

    PageDTO<Category> getAllCategories(Integer pageNumber, Integer pageSize, String active);

    Category createCategory(NewCategory newCategory);

    Category updateCategory(String id, UpdatedCategory updated);

    Inventory newInventory(NewInventory newInventory);

    Inventory updaInventory(UpdateInventory updateInventory);

    PageDTO<ProductResume> getListProductsWithInventory(int page, int size);

    PageDTO<Inventory> getHistoricInventoryByProduct(Long idProduct, Integer page, Integer size);

    List<Product> getAllProducts(String type);

}
