package com.backendIntegrador.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "El nombre de cliente no puede estar en blanco.")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "El nombre de cliente debe contener solo letras, números y guiones bajos.")
    @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres.")
    String clientName;

    @NotBlank(message = "El nombre de cliente no puede estar en blanco.")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "El nombre de cliente debe contener solo letras, números y guiones bajos.")
    @Size(min = 3, max = 30, message = "El nombre de cliente debe tener entre 3 y 30 caracteres.")
    String firstName;

    @NotBlank(message = "El nombre de cliente no puede estar en blanco.")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "El nombre de cliente debe contener solo letras, números y guiones bajos.")
    @Size(min = 3, max = 30, message = "El apellido de cliente debe tener entre 3 y 30 caracteres.")
    String lastName;

    @NotBlank(message = "La contraseña no puede estar en blanco.")
    @Size(min = 6, max = 30, message = "La contraseña debe tener entre 6 y 30 caracteres.")
    String password;

    @NotBlank(message = "El campo de correo electrónico no puede estar en blanco.")
    @Email( regexp = "^[\\w\\.-]+@[a-zA-Z\\d\\.-]+\\.[a-zA-Z]{2,}$",message = "El correo electrónico no es válido.")
    String email;
}
