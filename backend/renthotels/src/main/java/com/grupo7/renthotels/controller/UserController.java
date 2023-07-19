package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.dto.AuthDTO;
import com.grupo7.renthotels.model.dto.UserDTO;
import com.grupo7.renthotels.service.AuthService;
import com.grupo7.renthotels.service.TokenService;
import com.grupo7.renthotels.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) throws NotFoundException {
        return userService.saveUser(userDTO);
    }

    @CrossOrigin("*")
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody @Valid AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        String token = tokenService.generateToken(authentication);
        UserDTO userDTO = userService.findUserByEmail(authDTO.getEmail()).getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity.ok()
                .headers(headers)
                .body(userDTO);
    }

    @GetMapping
    public List<UserDTO> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping(params = "userEmail")
        public ResponseEntity<UserDTO> getUserEmail(@RequestParam(value = "userEmail") String userEmail){
            return userService.findUserByEmail(userEmail);
        }

    @GetMapping(params = "userSku")
    public ResponseEntity<UserDTO> getUserBySku(@RequestParam(value = "userSku") Long userSku){
        return userService.getUserBySku(userSku);
    }
}
