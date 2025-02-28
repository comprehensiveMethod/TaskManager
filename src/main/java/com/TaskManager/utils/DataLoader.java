package com.TaskManager.utils;

import com.TaskManager.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
    private final RoleService roleService;

    //на всякий случай вызвать мануально инитролей
    @Override
    public void run(String... args) throws Exception {
        roleService.initRoles();
    }
}
