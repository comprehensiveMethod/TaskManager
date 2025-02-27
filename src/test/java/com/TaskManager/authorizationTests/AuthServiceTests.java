package com.TaskManager.authorizationTests;

import com.TaskManager.dtos.JwtRequest;
import com.TaskManager.dtos.JwtResponse;
import com.TaskManager.dtos.RegistrationUserDto;
import com.TaskManager.models.User;
import com.TaskManager.services.AuthService;
import com.TaskManager.services.UserService;
import com.TaskManager.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private JwtRequest jwtRequest = new JwtRequest();
    private RegistrationUserDto registrationUserDto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jwtRequest.setEmail("user@example.com");
        jwtRequest.setPassword("password");
        registrationUserDto = new RegistrationUserDto();
        registrationUserDto.setEmail("user@example.com");
        registrationUserDto.setPassword("password");
        registrationUserDto.setConfirmPassword("password");
    }

    @Test
    public void testCreateAuthToken_ShouldReturnSuccess() {

        when(authenticationManager.authenticate(any())).thenReturn(null);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userService.loadUserByUsername(jwtRequest.getEmail())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("mocked_token");

        ResponseEntity<?> response = authService.createAuthToken(jwtRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(JwtResponse.class, response.getBody());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("mocked_token", jwtResponse.getToken());
    }

    @Test
    public void testCreateAuthToken_ShouldReturnBadCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Wrong password or email"));

        ResponseEntity<?> response = authService.createAuthToken(jwtRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Wrong password or email", response.getBody());
    }

    @Test
    public void testCreateUser_ShouldReturnSuccess() {
        // Mock user service behavior
        when(userService.findByUsername(registrationUserDto.getEmail())).thenReturn(java.util.Optional.empty());
        when(userService.createNewUser(any(RegistrationUserDto.class))).thenReturn(new User());

        ResponseEntity<?> response = authService.createUser(registrationUserDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCreateUser_ShouldReturnEmailAlreadyExists() {
        when(userService.findByUsername(registrationUserDto.getEmail())).thenReturn(java.util.Optional.of(new User()));

        ResponseEntity<?> response = authService.createUser(registrationUserDto);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Email already exists", response.getBody());
    }

    @Test
    public void testCreateUser_ShouldReturnInvalidEmail() {
        registrationUserDto.setEmail("invalid_email");

        ResponseEntity<?> response = authService.createUser(registrationUserDto);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Wrong email", response.getBody());
    }

    @Test
    public void testCreateUser_ShouldReturnPasswordMismatch() {
        registrationUserDto.setPassword("password");
        registrationUserDto.setConfirmPassword("different_password");

        ResponseEntity<?> response = authService.createUser(registrationUserDto);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Passwords don't match", response.getBody());
    }
}
