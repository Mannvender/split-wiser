package in.mannvender.splitwise.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}
