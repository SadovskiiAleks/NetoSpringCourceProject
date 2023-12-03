package com.example.courseprojectnetology.controller;

import com.example.courseprojectnetology.dto.LoginDTO;
import com.example.courseprojectnetology.models.User;
import com.example.courseprojectnetology.security.jwtMy.token.JWTTokenProvider;
import com.example.courseprojectnetology.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cloud")
public class LoginController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenProvider jwtTokenProvider;

    @Autowired
    public LoginController(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/get")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("Test");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        try {
            String login = loginDTO.getLogin();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, loginDTO.getPassword()));
            User user = userService.findByUsername(login);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + login + " not found");
            }

            String token = jwtTokenProvider.createToken(login, user.getRoles());
            String authToken = String.format("{\"auth-token\":\"%s\"}", token);
            return ResponseEntity.ok(authToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("auth-token") String authToken) {

        //jwtTokenProvider.resolveToken()

        return ResponseEntity.ok().build();
    }


}
