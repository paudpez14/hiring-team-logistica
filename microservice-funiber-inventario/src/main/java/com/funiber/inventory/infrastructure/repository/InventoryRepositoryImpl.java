package com.funiber.inventory.infrastructure.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

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
import com.funiber.inventory.domain.dto.UserResume;
import com.funiber.inventory.domain.repository.InventoryRepository;
import com.funiber.inventory.infrastructure.entity.inventory.CategoryEntity;
import com.funiber.inventory.infrastructure.entity.inventory.InventoryEntity;
import com.funiber.inventory.infrastructure.entity.inventory.ProductEntity;
import com.funiber.inventory.infrastructure.entity.inventory.UserEntity;
import com.funiber.inventory.infrastructure.repository.DB.CategoryRepositoryDB;
import com.funiber.inventory.infrastructure.repository.DB.InventoryRepositoryDB;
import com.funiber.inventory.infrastructure.repository.DB.ProductRepositoryDB;
import com.funiber.inventory.infrastructure.repository.DB.UserRepositoryDB;

import jakarta.persistence.criteria.Predicate;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {
    @Autowired
    ProductRepositoryDB productRepositoryDB;

    @Autowired
    CategoryRepositoryDB categoryRepositoryDB;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    InventoryRepositoryDB inventoryHistRepositoryDB;

    @Autowired
    UserRepositoryDB userRepositoryDB;

    @Override
    public ProductResume createProduct(NewProduct product) {

        ProductEntity productEntity = new ProductEntity();
        Optional<CategoryEntity> cOptional = categoryRepositoryDB.findById(product.getCategory_id());
        productEntity.setCategory(cOptional.get());
        productEntity.setCode(product.getCode());
        productEntity.setName(product.getName());
        productRepositoryDB.save(productEntity);
        ProductResume productResume = new ProductResume();
        Category category = modelMapper.map(cOptional.get(), Category.class);
        productResume.setCategory(category);
        productResume.setCode(product.getCode());
        productResume.setName(product.getName());
        return productResume;
    }

    @Override
    public PageDTO<ProductResume> getAllProductsResume(Integer pageNumber, Integer pageSize, String type) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("createdAt").descending());

        Page<ProductEntity> productPageJPA = null;
        Specification<ProductEntity> specification = (root, query, cb) -> {
            Predicate isActive;
            if ("ALL".equalsIgnoreCase(type)) {
                isActive = cb.or(
                        cb.equal(root.get("isActive"), "Y"),
                        cb.equal(root.get("isActive"), "N"));
            } else if ("act".equalsIgnoreCase(type)) {
                isActive = cb.equal(root.get("isActive"), "Y");
            } else if ("dect".equalsIgnoreCase(type)) {
                isActive = cb.equal(root.get("isActive"), "N");
            } else {
                throw new IllegalArgumentException("Invalid type parameter");
            }
            return isActive;
        };

        productPageJPA = productRepositoryDB.findAll(specification, pageable);

        Long totalProducts = productPageJPA.getTotalElements();

        PageDTO<ProductResume> prPageDTO = new PageDTO<>();
        prPageDTO.setCurrent(pageNumber);
        prPageDTO.setHasNext(productPageJPA.hasNext());
        prPageDTO.setHasPrevious(productPageJPA.hasPrevious());
        prPageDTO.setSizeData(totalProducts);
        prPageDTO.setNumPages(productPageJPA.getTotalPages());
        prPageDTO.setResults(modelMapper.map(productPageJPA.getContent(), new TypeToken<List<ProductResume>>() {
        }.getType()));

        return prPageDTO;
    }

    @Override
    public Optional<ProductResume> getProductByCodeWithInventory(String id) {
        List<Object[]> prOptional = productRepositoryDB.findProductByIdTableWithInventory(Long.parseLong(id));
        if (!prOptional.isEmpty()) {
            Object[] data = prOptional.get(0);
            Category category = new Category();
            Inventory inventory = new Inventory();
            UserResume user = modelMapper.map(userRepositoryDB.findById((Long) data[3]), UserResume.class);
            category.setId((Long) data[0]);
            category.setName((String) data[1]);
            inventory.setCreatedAt(((Timestamp) data[2]).toLocalDateTime());
            inventory.setCreatedBy(user);
            inventory.setHeight((double) data[4]);
            inventory.setIsActive((String) data[5]);
            inventory.setLength((double) data[6]);
            inventory.setStockQuantity((int) data[7]);
            inventory.setWidth((double) data[8]);
            inventory.setId((Long) data[9]);

            ProductResume productResume = new ProductResume();
            productResume.setCategory(category);
            productResume.setInventory(inventory);
            productResume.setCode((String) data[10]);
            productResume.setName((String) data[11]);
            return Optional.of(productResume);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Product updateProduct(Product product, UpdateProduct updateProduct, Category category) {

        modelMapper.map(updateProduct, product);
        CategoryEntity categoryEntity = modelMapper.map(category, CategoryEntity.class);
        ProductEntity productEntity = modelMapper.map(product, ProductEntity.class);
        productEntity.setCategory(categoryEntity);
        productEntity = productRepositoryDB.save(productEntity);
        product = modelMapper.map(productEntity, Product.class);
        return product;
    }

    @Override
    public Optional<Product> getProductByIdTable(String id) {
        Optional<ProductEntity> optional = productRepositoryDB.findById(Long.parseLong(id));
        Product product = modelMapper.map(optional.get(), Product.class);
        if (optional.isPresent()) {
            return Optional.of(product);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Product> getProductByCode(String id) {
        Optional<ProductEntity> optional = productRepositoryDB.findProductByCode(id);
        if (optional.isPresent()) {
            Product product = modelMapper.map(optional.get(), Product.class);
            return Optional.of(product);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Category> getCategoryById(Long category_id) {
        Optional<CategoryEntity> cOptional = categoryRepositoryDB.findById(category_id);
        if (cOptional.isPresent()) {
            return Optional.of(modelMapper.map(cOptional, Category.class));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteProduct(Product product) {
        ProductEntity prOptional = modelMapper.map(product, ProductEntity.class);
        prOptional.setIsActive("N");
        productRepositoryDB.save(prOptional);
    }

    @Override
    public PageDTO<Category> getAllCategories(Integer pageNumber, Integer pageSize, String type) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize,Sort.by("createdAt").descending());

        Page<CategoryEntity> categoryPageJPA = null;
        Specification<CategoryEntity> specification = (root, query, cb) -> {
            Predicate isActive;
            if ("ALL".equalsIgnoreCase(type)) {
                isActive = cb.or(
                        cb.equal(root.get("isActive"), "Y"),
                        cb.equal(root.get("isActive"), "N"));
            } else if ("act".equalsIgnoreCase(type)) {
                isActive = cb.equal(root.get("isActive"), "Y");
            } else if ("dect".equalsIgnoreCase(type)) {
                isActive = cb.equal(root.get("isActive"), "N");
            } else {
                throw new IllegalArgumentException("Invalid type parameter");
            }
            return isActive;
        };

        categoryPageJPA = categoryRepositoryDB.findAll(specification, pageable);

        Long totalCategories = categoryPageJPA.getTotalElements();

        PageDTO<Category> prPageDTO = new PageDTO<>();
        prPageDTO.setCurrent(pageNumber);
        prPageDTO.setHasNext(categoryPageJPA.hasNext());
        prPageDTO.setHasPrevious(categoryPageJPA.hasPrevious());
        prPageDTO.setSizeData(totalCategories);
        prPageDTO.setNumPages(categoryPageJPA.getTotalPages());
        prPageDTO.setResults(modelMapper.map(categoryPageJPA.getContent(), new TypeToken<List<Category>>() {
        }.getType()));

        return prPageDTO;
    }

    @Override
    public Category createCategory(NewCategory newCategory) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCode(newCategory.getCode());
        categoryEntity.setName(newCategory.getName());
        categoryEntity = categoryRepositoryDB.save(categoryEntity);
        return modelMapper.map(categoryEntity, Category.class);
    }

    @Override
    public boolean checkCategoryExists(String code) {
        return categoryRepositoryDB.checkCategoryExists(code);
    }

    @Override
    public Optional<Category> getCategoryByCode(String id) {
        Optional<CategoryEntity> optional = categoryRepositoryDB.findById(Long.valueOf(id));
        Category category = modelMapper.map(optional.get(), Category.class);
        if (optional.isPresent()) {
            return Optional.of(category);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Category updateCategory(Category category, UpdatedCategory updated) {
        modelMapper.map(updated, category);
        CategoryEntity categoryEntity = modelMapper.map(category, CategoryEntity.class);
        CategoryEntity result = categoryRepositoryDB.save(categoryEntity);
        Category categoryNew = modelMapper.map(result, Category.class);
        return categoryNew;
    }

    @Override
    public Inventory newInventory(Product product, User user, NewInventory newInventory) {
        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setIsActive("Y");
        inventoryEntity.setProduct(modelMapper.map(product, ProductEntity.class));
        inventoryEntity.setCreatedBy(modelMapper.map(user, UserEntity.class));
        inventoryEntity.setHeight(newInventory.getHeight());
        inventoryEntity.setLength(newInventory.getLength());
        inventoryEntity.setWidth(newInventory.getWidth());
        inventoryEntity.setStockQuantity(newInventory.getStockQuantity());
        inventoryEntity = inventoryHistRepositoryDB.save(inventoryEntity);
        return modelMapper.map(inventoryEntity, Inventory.class);

    }

    @Override
    public Inventory updateInventory(User updateUser, Inventory oldInventory, UpdateInventory updateInventory) {
        modelMapper.map(updateInventory, oldInventory);
        InventoryEntity inventoryEntity = modelMapper.map(oldInventory, InventoryEntity.class);
        inventoryEntity.setUpdatedAt(LocalDateTime.now());
        inventoryEntity.setUpdatedBy(modelMapper.map(updateUser, UserEntity.class));
        InventoryEntity result = inventoryHistRepositoryDB.save(inventoryEntity);
        Inventory inventoryNew = modelMapper.map(result, Inventory.class);
        return inventoryNew;
    }

    @Override
    public Optional<Inventory> getInventoryActiveById(Long id) {
        Optional<InventoryEntity> iOptional = inventoryHistRepositoryDB.findById(id);
        if (iOptional.isPresent()) {
            return Optional.of(modelMapper.map(iOptional.get(), Inventory.class));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public PageDTO<ProductResume> searchProductsByParams(String code, String name, String category,
            String hasStockString, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductEntity> productPageJPA = null;

        productPageJPA = productRepositoryDB.searchProductsByParams(code, name, category, pageable);
        Long totalProducts = productPageJPA.getTotalElements();

        PageDTO<ProductResume> prPageDTO = new PageDTO<>();
        prPageDTO.setCurrent(page);
        prPageDTO.setHasNext(productPageJPA.hasNext());
        prPageDTO.setHasPrevious(productPageJPA.hasPrevious());
        prPageDTO.setSizeData(totalProducts);
        prPageDTO.setNumPages(productPageJPA.getTotalPages());
        prPageDTO.setResults(modelMapper.map(productPageJPA.getContent(), new TypeToken<List<ProductResume>>() {
        }.getType()));

        return prPageDTO;
    }

    @Override
    public Optional<Inventory> getInventoryActiveByProductId(String id) {
        Optional<InventoryEntity> inveOptional = inventoryHistRepositoryDB.findInventoryActiveByProductCode(Long.valueOf(id));
        if (inveOptional.isPresent()) {
            return Optional.of(modelMapper.map(inveOptional, Inventory.class));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Inventory updateInventory(User updateUser, Inventory oldInventory) {
        InventoryEntity inventoryEntity = modelMapper.map(oldInventory, InventoryEntity.class);
        inventoryEntity.setUpdatedAt(LocalDateTime.now());
        inventoryEntity.setUpdatedBy(modelMapper.map(updateUser, UserEntity.class));
        InventoryEntity result = inventoryHistRepositoryDB.save(inventoryEntity);
        Inventory inventoryNew = modelMapper.map(result, Inventory.class);
        return inventoryNew;
    }

    @Override
    public PageDTO<ProductResume> getAllProductsWithInventory(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Object[]> productPageJPA = null;
        productPageJPA = productRepositoryDB.getProductsWithInventory(pageable);
        Long totalProducts = productPageJPA.getTotalElements();
        Collection<ProductResume> list = new ArrayList<>();
        for (Object[] object : productPageJPA.getContent()) {
            Category category = new Category();
            Inventory inventory = new Inventory();
            UserResume user = modelMapper.map(userRepositoryDB.findById((Long) object[3]), UserResume.class);
            UserResume userUpdated = null;

            category.setId((Long) object[0]);
            category.setName((String) object[1]);
            category.setCode((String) object[13]);
            category.setIsActive(String.valueOf(object[14]));

            inventory.setCreatedAt(((Timestamp) object[2]).toLocalDateTime());
            inventory.setHeight((double) object[4]);
            inventory.setIsActive((String) object[5]);
            inventory.setLength((double) object[6]);
            inventory.setStockQuantity((int) object[7]);
            inventory.setWidth((double) object[8]);
            inventory.setId((Long) object[9]);

            // Validación para el campo 15 (updatedAt)
            if (object[15] != null) {
                inventory.setUpdatedAt(((Timestamp) object[15]).toLocalDateTime());
            }

            // Validación para el campo 16 (updatedBy)
            if (object[16] != null) {
                userUpdated = modelMapper.map(userRepositoryDB.findById((Long) object[16]), UserResume.class);
            }

            inventory.setUpdatedBy(userUpdated);
            inventory.setCreatedBy(user);

            ProductResume productResume = new ProductResume();
            productResume.setId((Long) object[12]);
            productResume.setCategory(category);
            productResume.setInventory(inventory);
            productResume.setCode((String) object[10]);
            productResume.setName((String) object[11]);

            list.add(productResume);
        }

        PageDTO<ProductResume> prPageDTO = new PageDTO<>();
        prPageDTO.setCurrent(productPageJPA.getNumber() + 1);
        prPageDTO.setHasNext(productPageJPA.hasNext());
        prPageDTO.setHasPrevious(productPageJPA.hasPrevious());
        prPageDTO.setSizeData(totalProducts);
        prPageDTO.setNumPages(productPageJPA.getTotalPages());
        prPageDTO.setResults(list);
        return prPageDTO;
    }

    @Override
    public PageDTO<Inventory> getHistoricInventoryByProduct(Long idProduct, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<InventoryEntity> productPageJPA = null;
        productPageJPA = inventoryHistRepositoryDB.ListHistoricInventoryByProductCode(idProduct, pageable);
        Long totalProducts = productPageJPA.getTotalElements();
        PageDTO<Inventory> prPageDTO = new PageDTO<>();
        prPageDTO.setCurrent(page);
        prPageDTO.setHasNext(productPageJPA.hasNext());
        prPageDTO.setHasPrevious(productPageJPA.hasPrevious());
        prPageDTO.setSizeData(totalProducts);
        prPageDTO.setNumPages(productPageJPA.getTotalPages());
        prPageDTO.setResults(modelMapper.map(productPageJPA.getContent(), new TypeToken<List<Inventory>>() {
        }.getType()));
        return prPageDTO;

    }

    @Override
    public Optional<List<Product>> getAllProducts(String type) {
        Specification<ProductEntity> specification = (root, query, cb) -> {
            Predicate isActive;
            if ("ALL".equalsIgnoreCase(type)) {
                isActive = cb.or(
                        cb.equal(root.get("isActive"), "Y"),
                        cb.equal(root.get("isActive"), "N"));
            } else if ("act".equalsIgnoreCase(type)) {
                isActive = cb.equal(root.get("isActive"), "Y");
            } else if ("dect".equalsIgnoreCase(type)) {
                isActive = cb.equal(root.get("isActive"), "N");
            } else {
                throw new IllegalArgumentException("Invalid type parameter");
            }
            return isActive;
        };

        List<ProductEntity> result = productRepositoryDB.findAll(specification);

        if (result.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(modelMapper.map(result, new TypeToken<List<Product>>() {
            }.getType()));
        }
    }

}
