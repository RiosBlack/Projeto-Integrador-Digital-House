package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.User;
import com.grupo7.renthotels.model.dto.UserDTO;
import com.grupo7.renthotels.repository.FunctionRepository;
import com.grupo7.renthotels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FunctionRepository functionRepository;

    public ResponseEntity<UserDTO> saveUser(UserDTO userDTO) throws NotFoundException {

        Function function = functionRepository.findByName("User")
                .orElseThrow(() -> new NotFoundException("Função " + userDTO.getFunctionName() + " não existe"));

        User user = userDTO.toEntity();
        user.setUserSku(user.createSku());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoderPassword = encoder.encode(user.getPassword());
        user.setPassword(encoderPassword);
        user.setFunction(function);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.from(user));
    }

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream().map(UserDTO::from).collect(Collectors.toList());
    }

    public ResponseEntity<UserDTO> getUserBySku(Long sku){
        return userRepository.findByUserSku(sku).map(UserDTO::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(UserDTO::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
