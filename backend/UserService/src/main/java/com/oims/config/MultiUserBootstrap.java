package com.oims.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.oims.Model.User;
import com.oims.Model.User.Role;
import com.oims.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MultiUserBootstrap {
    
    private static final Logger log = LoggerFactory.getLogger(MultiUserBootstrap.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    // Admin configuration
    @Value("${users.admin.email:admin@oims.com}")
    private String adminEmail;
    @Value("${users.admin.password:admin123}")
    private String adminPassword;
    
    // Sales Executive configuration
    @Value("${users.sales.email:sales@oims.com}")
    private String salesEmail;
    @Value("${users.sales.password:sales123}")
    private String salesPassword;
    
    // Warehouse Manager configuration
    @Value("${users.warehouse.email:warehouse@oims.com}")
    private String warehouseEmail;
    @Value("${users.warehouse.password:warehouse123}")
    private String warehousePassword;
    
    // Finance Officer configuration
    @Value("${users.finance.email:finance@oims.com}")
    private String financeEmail;
    @Value("${users.finance.password:finance123}")
    private String financePassword;
    
    @PostConstruct
    public void createDefaultUsers() {
        log.info("Starting default users bootstrap...");
        
        createOrUpdateUser(adminEmail, adminPassword, Role.ADMIN, "Admin");
        createOrUpdateUser(salesEmail, salesPassword, Role.SALES_EXECUTIVE, "Sales Executive");
        createOrUpdateUser(warehouseEmail, warehousePassword, Role.WAREHOUSE_MANAGER, "Warehouse Manager");
        createOrUpdateUser(financeEmail, financePassword, Role.FINANCE_OFFICER, "Finance Officer");
        
        log.info("Default users bootstrap completed");
    }
    
    private void createOrUpdateUser(String email, String password, Role role, String roleName) {
        // Skip if email or password not configured
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            log.warn("{} email or password not configured. Skipping.", roleName);
            return;
        }
        
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            
            // Update role if different
            if (user.getRole() != role) {
                log.info("Updating user {} to {} role", email, roleName);
                user.setRole(role);
            }
            
            // Update password
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            
            log.info("{} user {} has been updated successfully", roleName, email);
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRole(role);
            userRepository.save(newUser);
            
            log.info("{} user {} has been created successfully", roleName, email);
        }
    }
}