package com.TaskManager.controllers;

import com.TaskManager.dtos.JwtRequest;
import com.TaskManager.dtos.JwtResponse;
import com.TaskManager.dtos.RegistrationUserDto;
import com.TaskManager.models.User;
import com.TaskManager.repositories.UserRepository;
import com.TaskManager.services.UserService;
import com.TaskManager.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    //TODO вынести логику в AuthService
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        }catch (BadCredentialsException e){
            return new ResponseEntity<>("Wrong password or email", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
    @PostMapping("/registration")
    public ResponseEntity<?> createUser(@RequestBody RegistrationUserDto registrationUserDto){
        if(!registrationUserDto.getPassword().equals((registrationUserDto.getConfirmPassword()))){
            return new ResponseEntity<>("Password don't match", HttpStatus.UNAUTHORIZED);
        }
        if(userService.findByUsername(registrationUserDto.getEmail()).isPresent()){
            return new ResponseEntity<>("Email already exists", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(user);
    }
}
