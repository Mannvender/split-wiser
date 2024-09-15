package in.mannvender.splitwise.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthSignupResponseDto {
    private Long id;
    private String name;
    private String email;
}
