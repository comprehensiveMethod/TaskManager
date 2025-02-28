package com.TaskManager.services;

import com.TaskManager.models.Role;
import com.TaskManager.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    /**
     * Создает роли 'USER' и 'ADMIN' при инициализации
     */
    @PostConstruct
    public void initRoles() {
        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("USER");
    }
    /**
     * Создает роль с именем, указанному в строке
     * @param name название роли для сохранения
     */
    private void createRoleIfNotFound(String name) {
        if (!roleRepository.findByName(name).isPresent()) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }
}
