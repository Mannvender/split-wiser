package in.mannvender.splitwise.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthValidateTokenResponseDto {
    private boolean isTokenValid;
    private Long userId;
}
