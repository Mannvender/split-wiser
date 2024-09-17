package in.mannvender.splitwise.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthValidateTokenRequestDto {
    private String token;
    private Long userId;
}
