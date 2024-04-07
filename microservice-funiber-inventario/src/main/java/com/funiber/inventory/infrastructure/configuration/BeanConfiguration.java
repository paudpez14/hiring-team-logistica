package com.funiber.inventory.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.funiber.inventory.DomainLayerApplication;
import com.funiber.inventory.domain.repository.InventoryRepository;
import com.funiber.inventory.domain.repository.UserRepository;
import com.funiber.inventory.domain.service.InventoryService;
import com.funiber.inventory.domain.service.UserService;
import com.funiber.inventory.domain.service.impl.InventoryServiceImpl;
import com.funiber.inventory.domain.service.impl.UserServiceImpl;

@Configuration
@ComponentScan(basePackageClasses = DomainLayerApplication.class)
public class BeanConfiguration {
    @Bean
    UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }
    @Bean
    InventoryService inventoryService(InventoryRepository inventoryRepository, UserRepository userRepository) {
        return new InventoryServiceImpl(inventoryRepository, userRepository);
    }

}
