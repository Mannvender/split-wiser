package in.mannvender.splitwise.dtos.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private String name;
    private String email;
    private String password;
}
