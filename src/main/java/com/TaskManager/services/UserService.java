package com.TaskManager.services;

import com.TaskManager.dtos.RegistrationUserDto;
import com.TaskManager.models.User;
import com.TaskManager.repositories.RoleRepository;
import com.TaskManager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Загружает пользователя по Email
     * @param email Email пользователя
     * @return {@code User}, содержащий данные о пользователе
     */
    public Optional<User> findByUsername(String email){
        return userRepository.findByEmail(email);
    }
    /**
     * Загружает пользователя по Email
     * @param username Email пользователя
     * @return {@code UserDetails}, содержащий данные о загруженном пользователе
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "пользователь не найден"
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }
    /**
     * Создает пользователя с ролью 'USER'
     * @param registrationUserDto данные регистрации пользователя
     * @return {@code User}, содержащий данные о сохраненном в базе данных пользователе
     */
    public User createNewUser(RegistrationUserDto registrationUserDto){
        User user = new User();
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));

        user.setRoles(List.of(roleRepository.findByName("USER").get()));

        return userRepository.save(user);
    }
}
