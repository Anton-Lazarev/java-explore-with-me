package ru.practicum.ewm.main.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDTO {
    private long id;
    @NotBlank(message = "Name of user can't be blank, empty or null")
    @Size(min = 2, max = 250, message = "Size of name should be between 2 and 250")
    private String name;
    @NotBlank(message = "Email of user can't be blank, empty or null")
    @Size(min = 6, max = 254, message = "Size of email should be between 6 and 254")
    @Email
    private String email;
}
