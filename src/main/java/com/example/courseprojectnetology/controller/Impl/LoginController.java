package com.example.courseprojectnetology.controller.Impl;

import com.example.courseprojectnetology.dto.LoginAndPasswordDTO;
import com.example.courseprojectnetology.dto.LoginDTO;
import com.example.courseprojectnetology.exception.errors.BadRequestError;
import com.example.courseprojectnetology.models.User;
import com.example.courseprojectnetology.security.jwtMy.token.JWTTokenProvider;
import com.example.courseprojectnetology.service.Impl.ExceptionSingletonServiceImpl;
import com.example.courseprojectnetology.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cloud")
@Slf4j
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
    public LoginDTO login(@RequestBody() LoginAndPasswordDTO loginAndPasswordDTO) {
        try {
            String login = loginAndPasswordDTO.getLogin();
            log.info("Authentication user with username: " + login);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, loginAndPasswordDTO.getPassword()));
            User user = userService.findByUsername(login);
            if (user == null) {
                log.info("User with username: " + login + " not found");
                throw new UsernameNotFoundException("User with username: " + login + " not found");
            }
            String token = jwtTokenProvider.createToken(login, user.getRoles());
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setAuthToken(token);
            return loginDTO;
        } catch (AuthenticationException e) {
            throw new BadRequestError("Bad credentials", ExceptionSingletonServiceImpl.getInstance().getId());
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestHeader("auth-token") String authToken) {
        jwtTokenProvider.refreshToken(authToken);
        return "Success logout";
    }


}
