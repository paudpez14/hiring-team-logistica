package com.funiber.inventory.domain.repository;

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
import com.funiber.inventory.domain.dto.User;

public interface InventoryRepository {
    public ProductResume createProduct(NewProduct product);

    public PageDTO<ProductResume> getAllProductsResume(Integer pageNumber, Integer pageSize, String type);

    public Optional<ProductResume> getProductByCodeWithInventory(String id);

    public Optional<Product> getProductByIdTable(String id);

    public Optional<Product> getProductByCode(String id);

    public Product updateProduct(Product product, UpdateProduct updateProduct, Category category);

    public Optional<Category> getCategoryById(Long category_id);

    public void deleteProduct(Product product);

    public PageDTO<Category> getAllCategories(Integer pageNumber, Integer pageSize, String active);

    public boolean checkCategoryExists(String code);

    public Category createCategory(NewCategory newCategory);

    public Optional<Category> getCategoryByCode(String id);

    public Category updateCategory(Category category, UpdatedCategory updated);

    public Inventory newInventory(Product product, User user, NewInventory newInventory);

    public Inventory updateInventory(User updateUser, Inventory oldInventory, UpdateInventory updateInventory);

    public Inventory updateInventory(User updateUser, Inventory oldInventory);

    public Optional<Inventory> getInventoryActiveById(Long id);

    public Optional<Inventory> getInventoryActiveByProductId(String id);

    public PageDTO<ProductResume> searchProductsByParams(String code, String name, String category,
            String hasStockString, int page, int size);

    public PageDTO<ProductResume> getAllProductsWithInventory(int page, int size);

    public PageDTO<Inventory> getHistoricInventoryByProduct(Long idProduct, Integer page, Integer size);

    public Optional<List<Product>> getAllProducts(String type);
}
