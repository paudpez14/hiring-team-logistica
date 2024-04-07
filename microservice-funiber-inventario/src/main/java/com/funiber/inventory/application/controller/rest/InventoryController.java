package com.funiber.inventory.application.controller.rest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.funiber.inventory.application.dto.request.NewCategoryDTO;
import com.funiber.inventory.application.dto.request.NewInventoryDTO;
import com.funiber.inventory.application.dto.request.NewProductDTO;
import com.funiber.inventory.application.dto.request.UpdateInventoryDTO;
import com.funiber.inventory.application.dto.response.CategoryRespDTO;
import com.funiber.inventory.application.dto.response.InventoryRespDTO;
import com.funiber.inventory.application.dto.response.PageDTO;
import com.funiber.inventory.application.dto.response.ProductRespDTO;
import com.funiber.inventory.application.dto.response.ProductResumeRespDTO;
import com.funiber.inventory.application.dto.response.StandardResponse;
import com.funiber.inventory.application.service.JwtService;
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
import com.funiber.inventory.domain.exception.CategoryNotFoundByCode;
import com.funiber.inventory.domain.exception.ProductExistException;
import com.funiber.inventory.domain.exception.ProductNotFoundByCode;
import com.funiber.inventory.domain.exception.ProductNotFoundByID;
import com.funiber.inventory.domain.service.InventoryService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/inventory-management")
public class InventoryController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtService jwtService;
    InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email, @RequestParam(defaultValue = "Y") String type) {
        StandardResponse<List<ProductRespDTO>> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            List<Product> dto = inventoryService.getAllProducts(type);
            List<ProductRespDTO> data = modelMapper.map(dto, new TypeToken<List<ProductRespDTO>>() {
            }.getType());
            response.setMessage("Lista de Productos");
            response.setStatus("Founded");
            response.setHttpStatus(HttpStatus.OK.value());
            response.setData(data);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/products-resume")
    public ResponseEntity<?> getAllProductsResume(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "Y") String type) {
        StandardResponse<PageDTO<ProductResumeRespDTO>> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            PageDTO<ProductResume> dto = inventoryService.getAllProductsResume(page, size, type);
            PageDTO<ProductResumeRespDTO> data = new PageDTO<>();
            data.setCurrent(page);
            data.setHasNext(dto.isHasNext());
            data.setHasPrevious(dto.isHasPrevious());
            data.setNumPages(dto.getNumPages());
            data.setResults(modelMapper.map(dto.getResults(), new TypeToken<List<ProductResumeRespDTO>>() {
            }.getType()));
            data.setSizeData(dto.getSizeData());
            response.setMessage("Lista de Productos");
            response.setStatus("Founded");
            response.setHttpStatus(HttpStatus.OK.value());
            response.setData(data);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/products")
    public ResponseEntity<?> createProducts(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email,
            @RequestBody NewProductDTO prd) {

        StandardResponse<ProductResumeRespDTO> response = new StandardResponse<>();
        NewProduct newProduct = modelMapper.map(prd, NewProduct.class);
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            ProductResume productResume = inventoryService.createProduct(newProduct);
            response.setMessage("Producto creado exitosamente");
            response.setStatus("Created");
            response.setHttpStatus(HttpStatus.CREATED.value());
            ProductResumeRespDTO pRespDTO = modelMapper.map(productResume, ProductResumeRespDTO.class);
            response.setData(pRespDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ProductExistException e) {
            response.setStatus("Incorrect");
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductByCode(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email, @PathVariable("id") String id) {

        StandardResponse<ProductResume> response = new StandardResponse<>();
        String cleanToken = token.replace("Bearer ", "");
        if (!jwtService.validateToken(cleanToken, email)) {
            response.setMessage("Token inválido o expirado");
            response.setHttpStatus(511);
            return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
        }
        Optional<ProductResume> result = inventoryService.getProductByCodeWithInventory(id);
        response.setMessage("Producto encontrado exitosamente");
        response.setStatus("Founded");
        response.setHttpStatus(HttpStatus.OK.value());
        response.setData(result.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProductByID(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email, @PathVariable("id") String id,
            @RequestBody NewProductDTO entity) {
        StandardResponse<Product> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            Product product = inventoryService.updateProduct(id, modelMapper.map(entity, UpdateProduct.class));
            response.setMessage("Producto actualizado correctamente");
            response.setStatus("Updated");
            response.setData(product);
            response.setHttpStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ProductNotFoundByID e) {
            response.setStatus("Not found");
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProductById(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email, @PathVariable("id") String id) {
        StandardResponse<?> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            inventoryService.deleteProduct(id);
            response.setMessage("Producto eliminado correctamente");
            response.setStatus("Deleted");
            response.setHttpStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ProductNotFoundByID e) {
            response.setStatus("Not found");
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<?> searchProducts(
            @RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "false") Boolean hasStock,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        StandardResponse<PageDTO<ProductResumeRespDTO>> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            PageDTO<ProductResume> dto = inventoryService.searchProductsByParams(code, name, category, hasStock, page,
                    size);
            PageDTO<ProductResumeRespDTO> data = new PageDTO<>();
            data.setCurrent(page);
            data.setHasNext(dto.isHasNext());
            data.setHasPrevious(dto.isHasPrevious());
            data.setNumPages(dto.getNumPages());
            data.setResults(modelMapper.map(dto.getResults(), new TypeToken<List<ProductResumeRespDTO>>() {
            }.getType()));
            data.setSizeData(dto.getSizeData());
            response.setMessage("Lista de Productos");
            response.setStatus("Founded");
            response.setHttpStatus(HttpStatus.OK.value());
            response.setData(data);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "ALL") String type) {
        StandardResponse<PageDTO<CategoryRespDTO>> response = new StandardResponse<>();
        try {
            PageDTO<Category> dto = inventoryService.getAllCategories(page, size, type);
            PageDTO<CategoryRespDTO> data = new PageDTO<>();
            data.setCurrent(page);
            data.setHasNext(dto.isHasNext());
            data.setHasPrevious(dto.isHasPrevious());
            data.setNumPages(dto.getNumPages());
            data.setResults(modelMapper.map(dto.getResults(), new TypeToken<List<CategoryRespDTO>>() {
            }.getType()));
            data.setSizeData(dto.getSizeData());
            response.setMessage("Lista de Categoriasx|");
            response.setStatus("Founded");
            response.setHttpStatus(HttpStatus.OK.value());
            response.setData(data);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategories(@RequestHeader(name = "Authorization") String token,
    @RequestHeader(name = "Email") String email,@RequestBody NewCategoryDTO body) {
        StandardResponse<CategoryRespDTO> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            NewCategory newCategory = modelMapper.map(body, NewCategory.class);
            Category category = inventoryService.createCategory(newCategory);
            response.setMessage("Categoria creada exitosamente");
            response.setStatus("Created");
            response.setHttpStatus(HttpStatus.CREATED.value());
            CategoryRespDTO pRespDTO = modelMapper.map(category, CategoryRespDTO.class);
            response.setData(pRespDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategorie(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email, @PathVariable("id") String id,
            @RequestBody NewCategoryDTO entity) {
        StandardResponse<CategoryRespDTO> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            Category data = inventoryService.updateCategory(id, modelMapper.map(entity, UpdatedCategory.class));
            response.setMessage("Categoria actualizado correctamente");
            response.setStatus("Updated");
            response.setData(modelMapper.map(data, CategoryRespDTO.class));
            response.setHttpStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CategoryNotFoundByCode e) {
            response.setStatus("Not found");
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/historic-inventory/{id}")
    public ResponseEntity<?> getHistoricInventory(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email, @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size, @PathVariable("id") Long id) {
        StandardResponse<PageDTO<InventoryRespDTO>> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            PageDTO<Inventory> list = inventoryService.getHistoricInventoryByProduct(id, page, size);
            PageDTO<InventoryRespDTO> dataPage = new PageDTO<>();
            dataPage.setCurrent(list.getCurrent());
            dataPage.setHasNext(list.isHasNext());
            dataPage.setHasPrevious(list.isHasPrevious());
            dataPage.setNumPages(list.getNumPages());
            dataPage.setResults(modelMapper.map(list.getResults(), new TypeToken<List<InventoryRespDTO>>() {
            }.getType()));
            dataPage.setSizeData(list.getSizeData());
            response.setMessage("Lista de Historicos");

            response.setStatus("Founded");
            response.setData(dataPage);
            response.setHttpStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list-inventory")
    public ResponseEntity<?> getListInventory(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email, @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        StandardResponse<PageDTO<ProductResume>> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            PageDTO<ProductResume> list = inventoryService.getListProductsWithInventory(page, size);
            response.setMessage("Inventario creado correctamente");
            response.setStatus("Created");
            response.setData(list);
            response.setHttpStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register-inventory")
    public ResponseEntity<?> registerInventory(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email, @RequestBody NewInventoryDTO entity) {
        StandardResponse<InventoryRespDTO> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            Inventory inventory = inventoryService.newInventory(modelMapper.map(entity, NewInventory.class));
            response.setMessage("Inventario creado correctamente");
            response.setStatus("Created");
            response.setData(modelMapper.map(inventory, InventoryRespDTO.class));
            response.setHttpStatus(HttpStatus.CREATED.value());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ProductNotFoundByCode e) {
            response.setStatus("Not found");
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-inventory")
    public ResponseEntity<?> putMethodName(@RequestHeader(name = "Authorization") String token,
            @RequestHeader(name = "Email") String email, @RequestBody UpdateInventoryDTO entity) {
        StandardResponse<InventoryRespDTO> response = new StandardResponse<>();
        try {
            String cleanToken = token.replace("Bearer ", "");
            if (!jwtService.validateToken(cleanToken, email)) {
                response.setMessage("Token inválido o expirado");
                response.setHttpStatus(511);
                return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            }
            Inventory inventory = inventoryService.updaInventory(modelMapper.map(entity, UpdateInventory.class));
            response.setMessage("Inventario actualizado correctamente");
            response.setStatus("Updated");
            response.setData(modelMapper.map(inventory, InventoryRespDTO.class));
            response.setHttpStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ProductNotFoundByID e) {
            response.setStatus("Not found");
            response.setMessage(e.getMessage());
            response.setHttpStatus(HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
