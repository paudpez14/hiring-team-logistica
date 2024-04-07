package com.funiber.inventory.infrastructure.seeds;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.funiber.inventory.infrastructure.entity.inventory.CategoryEntity;
import com.funiber.inventory.infrastructure.entity.inventory.InventoryEntity;
import com.funiber.inventory.infrastructure.entity.inventory.ProductEntity;
import com.funiber.inventory.infrastructure.entity.inventory.UserEntity;
import com.funiber.inventory.infrastructure.repository.DB.CategoryRepositoryDB;
import com.funiber.inventory.infrastructure.repository.DB.InventoryRepositoryDB;
import com.funiber.inventory.infrastructure.repository.DB.ProductRepositoryDB;
import com.funiber.inventory.infrastructure.repository.DB.UserRepositoryDB;

@Component
public class Seeders implements CommandLineRunner {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepositoryDB userRepositoryDB;

    @Autowired
    private ProductRepositoryDB productRepository;

    @Autowired
    private CategoryRepositoryDB categoryRepository;

    @Autowired
    private InventoryRepositoryDB inventoryRepositoryDB;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Ejecutando Seeders...");
        // Crear usuario si no existe
        createUserIfNotExists();
        // Ejecutar CategorySeeder
        runCategorySeeder();

        // Ejecutar ProductSeeder
        runProductSeeder();
    }

    private void createUserIfNotExists() {
        System.out.println("Creando usuario si no existe...");
        Optional<UserEntity> existingUser = userRepositoryDB.findUserByEmail("funiber_admin@mail.com");
        if (existingUser.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setEmail("funiber_admin@mail.com");
            String encryptedPassword = passwordEncoder.encode("adminsecret");
            user.setPassword(encryptedPassword);
            userRepositoryDB.save(user);
            System.out.println("Usuario creado exitosamente.");
        } else {
            System.out.println("El usuario ya existe en la base de datos.");
        }
    }

    private void runCategorySeeder() {
        System.out.println("Ejecutando CategorySeeder...");
        List<String> categories = new ArrayList<>();
        // Agregar 20 categorías diferentes en español
        categories.add("Electrónica");
        categories.add("Ropa");
        categories.add("Muebles");
        categories.add("Juguetes");
        categories.add("Hogar");
        categories.add("Deportes");
        categories.add("Automóviles");
        categories.add("Libros");
        categories.add("Alimentos");
        categories.add("Bebidas");
        categories.add("Tecnología");
        categories.add("Salud y belleza");
        categories.add("Mascotas");
        categories.add("Instrumentos musicales");
        categories.add("Jardinería");
        categories.add("Arte y manualidades");
        categories.add("Viajes");
        categories.add("Herramientas");
        categories.add("Juegos");
        categories.add("Software");

        if (categoryRepository.count() == 0) {
            // Generar las categorías solo si no hay ninguna en la base de datos
            int index = 1;
            for (String categoryName : categories) {
                CategoryEntity category = new CategoryEntity();
                category.setCode(String.valueOf(index));
                category.setName(categoryName);
                categoryRepository.save(category);
                index++;
            }
            System.out.println("Data seeding completed for categories.");
        } else {
            System.out.println("Categories already exist in the database.");
        }
    }

    private void runProductSeeder() {
        System.out.println("Ejecutando ProductSeeder...");
        long productCount = productRepository.count();
        if (productCount >= 30) {
            System.out.println("Ya existen 30 productos en la base de datos.");
            return;
        }

        List<CategoryEntity> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            System.out.println("No hay categorías disponibles en la base de datos.");
            return;
        }

        Random random = new Random();
        while (productCount < 30) {
            ProductEntity product = new ProductEntity();
            product.setName(generateProductName());
            product.setCode(generateProductCode());
            product.setCategory(getRandomCategory(categories));
            productRepository.save(product);

            createInventoryRecords(product);

            productCount++;
        }

        System.out.println("Data seeding completed for products.");
    }

    private String generateProductCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateProductName() {
        return "Producto " + (productRepository.count() + 1);
    }

    private CategoryEntity getRandomCategory(List<CategoryEntity> categories) {
        Random random = new Random();
        return categories.get(random.nextInt(categories.size()));
    }

    private void createInventoryRecords(ProductEntity product) {
        LocalDateTime currentDate = LocalDateTime.now();
        Random random = new Random();
        Optional<UserEntity> user = userRepositoryDB.findUserByEmail("funiber_admin@mail.com");
        for (int i = 0; i < 6; i++) {
            InventoryEntity inventory = new InventoryEntity();
            inventory.setProduct(product);
            inventory.setStockQuantity(random.nextInt(100));
            inventory.setLength(random.nextDouble() * 100);
            inventory.setWidth(random.nextDouble() * 100);
            inventory.setHeight(random.nextDouble() * 100);
            inventory.setCreatedAt(currentDate.minusDays(i * 10));
            inventory.setCreatedBy(user.get());
            if (i == 0) {
                inventory.setIsActive("Y");
            } else {
                inventory.setIsActive("N");
                inventory.setUpdatedBy(user.get());
                inventory.setUpdatedAt(currentDate.plusDays(i * 15));
            }
            inventoryRepositoryDB.save(inventory);
        }
    }
}
