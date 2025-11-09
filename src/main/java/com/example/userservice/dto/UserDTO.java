package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private Long id;

    @NotBlank(message = "Не должно быть пустым")
    private String name;

    @NotBlank(message = "Не должно быть пустым")
    private String email;

    @NotNull(message = "Не должно быть пустым")
    @Min(value = 0, message = "Должно быть больше или равно 0")
    private Integer age;

    public UserDTO() {

    }

    public UserDTO(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

}
