package in.mannvender.splitwise.dtos.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginResponseDto {
    private Long id;
    private String name;
    private String email;
}
