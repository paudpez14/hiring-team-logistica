package com.funiber.inventory.domain.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.web.servlet.tags.form.OptionTag;

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
import com.funiber.inventory.domain.exception.CategoryExistException;
import com.funiber.inventory.domain.exception.CategoryNotFoundByCode;
import com.funiber.inventory.domain.exception.ProductExistException;
import com.funiber.inventory.domain.exception.ProductNotFoundByCode;
import com.funiber.inventory.domain.repository.InventoryRepository;
import com.funiber.inventory.domain.repository.UserRepository;
import com.funiber.inventory.domain.service.InventoryService;

public class InventoryServiceImpl implements InventoryService {
    InventoryRepository inventoryRepository;
    UserRepository userRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, UserRepository userRepository) {
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProductResume createProduct(NewProduct product) {
        if ((inventoryRepository.getProductByCode(product.getCode())).isPresent()) {
            throw new ProductExistException(product.getCode());
        }
        return inventoryRepository.createProduct(product);
    }

    @Override
    public PageDTO<ProductResume> getAllProductsResume(Integer pageNumber, Integer pageSize, String type) {
        return inventoryRepository.getAllProductsResume(pageNumber, pageSize, type);
    }

    @Override
    public Optional<ProductResume> getProductByCodeWithInventory(String id) {
        return inventoryRepository.getProductByCodeWithInventory(id);
    }

    @Override
    public Product updateProduct(String id, UpdateProduct updateProduct) {
        Optional<Product> product = inventoryRepository.getProductByIdTable(id);

        if (product.isPresent()) {
            Optional<Category> cOptional = inventoryRepository.getCategoryById(updateProduct.getCategory_id());
            return inventoryRepository.updateProduct(product.get(), updateProduct, cOptional.get());
        } else {
            throw new CategoryNotFoundByCode(updateProduct.getCode());
        }

    }

    @Override
    public Product getProductByCode(String id) {

        Optional<Product> product = inventoryRepository.getProductByIdTable(id);

        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ProductNotFoundByCode(id);
        }

    }

    @Override
    public void deleteProduct(String id) {
        Optional<Product> product = inventoryRepository.getProductByIdTable(id);
        if (product.isPresent()) {
            inventoryRepository.deleteProduct(product.get());
        } else {
            throw new ProductNotFoundByCode(id);
        }
    }

    @Override
    public PageDTO<ProductResume> searchProductsByParams(String code, String name, String category, Boolean hasStock,
            int page,
            int size) {
        String hasStockString = "N";
        if (hasStock) {
            hasStockString = "Y";
        }
        return inventoryRepository.searchProductsByParams(code, name, category, hasStockString, page, size);
    }

    @Override
    public PageDTO<Category> getAllCategories(Integer pageNumber, Integer pageSize, String active) {
        return inventoryRepository.getAllCategories(pageNumber, pageSize, active);

    }

    @Override
    public Category createCategory(NewCategory newCategory) {
        if (inventoryRepository.checkCategoryExists(newCategory.getCode())) {
            throw new CategoryExistException(newCategory.getCode());
        }
        return inventoryRepository.createCategory(newCategory);
    }

    @Override
    public Category updateCategory(String id, UpdatedCategory updated) {
        Optional<Category> category = inventoryRepository.getCategoryByCode(id);
        if (category.isPresent()) {
            return inventoryRepository.updateCategory(category.get(), updated);
        } else {
            throw new CategoryNotFoundByCode(updated.getCode());
        }
    }

    @Override
    public Inventory newInventory(NewInventory newInventory) {
        Optional<Product> product = inventoryRepository.getProductByIdTable(newInventory.getIdProduct());
        if (product.isPresent()) {
            Optional<Inventory> oldInventory = inventoryRepository
                    .getInventoryActiveByProductId(newInventory.getIdProduct());
            Optional<User> user = userRepository.findUserByEmail(newInventory.getCreatedBy());
            if (oldInventory.isPresent()) {
                oldInventory.get().setIsActive("N");
                inventoryRepository.updateInventory(user.get(), oldInventory.get());
            }

            return inventoryRepository.newInventory(product.get(), user.get(), newInventory);
        } else {
            throw new ProductNotFoundByCode(newInventory.getIdProduct());
        }
    }

    @Override
    public Inventory updaInventory(UpdateInventory updateInventory) {
        Optional<User> userOptional = userRepository.findUserByEmail(updateInventory.getCreatedBy());
        Optional<Inventory> oldInvOptional = inventoryRepository
                .getInventoryActiveById(updateInventory.getIdInventory());
        return inventoryRepository.updateInventory(userOptional.get(), oldInvOptional.get(), updateInventory);
    }

    @Override
    public PageDTO<ProductResume> getListProductsWithInventory(int page, int size) {
        return inventoryRepository.getAllProductsWithInventory(page, size);
    }

    @Override
    public PageDTO<Inventory> getHistoricInventoryByProduct(Long idProduct, Integer page, Integer size) {
        return inventoryRepository.getHistoricInventoryByProduct(idProduct, page, size);

    }

    @Override
    public List<Product> getAllProducts(String type) {
        Optional<List<Product>> optional = inventoryRepository.getAllProducts(type);
        if (optional.isPresent()) {
            return optional.get();

        } else {
            return new ArrayList<>();
        }
    }

}
