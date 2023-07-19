package com.grupo7.renthotels.model.dto;

import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.User;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userSku;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String functionName;

    public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserSku(user.getUserSku());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEmail(user.getEmail());
        userDTO.setFunctionName(user.getFunction().getName());
        return userDTO;
    }

    public User toEntity() {
        User user = new User();
        user.setUserSku(this.userSku);
        user.setName(this.name);
        user.setSurname(this.surname);
        user.setEmail(this.email);
        user.setPassword(this.password);
        return user;
    }
}
