package in.mannvender.splitwise.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginRequestDto {
    private String email;
    private String password;
}
