package com.funiber.inventory.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {    
    @Email(message = "Correo electrónico inválido")
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
