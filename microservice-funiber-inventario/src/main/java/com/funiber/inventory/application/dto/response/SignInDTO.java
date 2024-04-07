package com.funiber.inventory.application.dto.response;
import lombok.Data;
@Data
public class SignInDTO {
    private UserRespDTO user;
    private String token;
}
